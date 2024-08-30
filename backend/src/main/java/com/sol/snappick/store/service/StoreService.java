package com.sol.snappick.store.service;

import com.sol.snappick.member.entity.Member;
import com.sol.snappick.member.service.BasicMemberService;
import com.sol.snappick.store.dto.StoreCreateReq;
import com.sol.snappick.store.dto.StoreRes;
import com.sol.snappick.store.dto.StoreSearchReq;
import com.sol.snappick.store.dto.StoreUpdateReq;
import com.sol.snappick.store.entity.Store;
import com.sol.snappick.store.entity.StoreImage;
import com.sol.snappick.store.exception.StoreImageLimitExceedException;
import com.sol.snappick.store.exception.StoreIsNotYoursException;
import com.sol.snappick.store.exception.StoreNotFoundException;
import com.sol.snappick.store.mapper.StoreImageMapper;
import com.sol.snappick.store.mapper.StoreMapper;
import com.sol.snappick.store.mapper.StoreRunningTimeMapper;
import com.sol.snappick.store.mapper.StoreTagMapper;
import com.sol.snappick.store.repository.StoreImageRepository;
import com.sol.snappick.store.repository.StoreRepository;
import com.sol.snappick.store.repository.StoreRunningTimeRepository;
import com.sol.snappick.store.repository.StoreTagRepository;
import com.sol.snappick.util.minio.ImageUploadRes;
import com.sol.snappick.util.minio.MinioUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {

    public final StoreRepository storeRepository;
    public final StoreImageRepository storeImageRepository;
    public final StoreRunningTimeRepository storeRunningRepository;
    public final StoreTagRepository storeTagRepository;

    public final StoreMapper storeMapper;
    public final StoreTagMapper storeTagMapper;
    public final StoreRunningTimeMapper storeRunningTimeMapper;
    public final StoreImageMapper storeImageMapper;

    // MINIO 버킷 이름
    private final String BUCKET_NAME = "snappick-store";
    private final MinioUtil minioUtil;
    private final BasicMemberService basicMemberService;

    /**
     * pop up store create 서비스 로직
     *
     * @param storeCreateReq
     * @param images
     * @param memberId
     * @return
     * @throws Exception
     */
    @Transactional
    public StoreRes createPopupStore(
            StoreCreateReq storeCreateReq,
            MultipartFile[] images,
            Integer memberId
    ) throws Exception {
        if(images != null && images.length > 3) {
            throw new StoreImageLimitExceedException();
        }

		Member member = basicMemberService.getMemberById(memberId);
		storeCreateReq.setSellerId(memberId);

		Store storeToCreate = createStoreWithDetails(storeCreateReq);

		storeToCreate.setMember(member); // Member 설정

		storeToCreate = storeRepository.save(storeToCreate); // Store 먼저 저장
		storeToCreate.updateStatus(); // status 계산
		// 이미지 처리 및 저장
		if(images != null) {
			List<StoreImage> storeImages = uploadImagesToMinio(images,
															   storeToCreate);
			storeImageRepository.saveAll(storeImages);
			storeToCreate.setImages(storeImages);
		}

        return storeMapper.toDto(storeToCreate);
    }

    /**
     * MINIO 에 이미지 업로드
     *
     * @param images image file
     * @param store  스토어
     * @return List<StoreImage>
     * @throws Exception minio Exception
     */
    private List<StoreImage> uploadImagesToMinio(
            MultipartFile[] images,
            Store store
    ) throws Exception {
        List<StoreImage> storeImages = new ArrayList<>();

        for(MultipartFile image : images) {
            if(image.isEmpty()) {
                continue;
            }
            // 실제 이미지 파일 저장 로직은 생략합니다 (예: 파일 시스템, S3, etc.)
            ImageUploadRes imageDto = minioUtil.uploadImageWithThumbnail(BUCKET_NAME,
                                                                         image
            ); // 이미지 저장 메서드 필요
            if(imageDto == null) {
                continue;
            }
            StoreImage storeImage = StoreImage.builder()
                                              .originImageUrl(imageDto.getOriginImageUrl())
                                              .thumbnailImageUrl(imageDto.getThumbnailImageUrl())
                                              .store(store)
                                              .build();
            storeImages.add(storeImage);
        }
        return storeImages;
    }

    /**
     * store 생성
     *
     * @param storeCreateReq
     * @return
     */
    private Store createStoreWithDetails(StoreCreateReq storeCreateReq) {
        // Store 엔티티 생성 및 태그, 이미지, 운영 시간 설정
        Store storeToCreate = storeMapper.toEntity(storeCreateReq);
        storeToCreate.setTags(storeTagMapper.toEntityList(storeCreateReq.getTags(), storeToCreate));
        storeToCreate.setImages(
                storeImageMapper.toEntityList(storeCreateReq.getImages(), storeToCreate));
        storeToCreate.setRunningTimes(
                storeRunningTimeMapper.toEntityList(storeCreateReq.getRunningTimes(),
                                                    storeToCreate
                ));
        return storeToCreate;
    }

    /**
     * 내 store 모아보기
     *
     * @param memberId
     * @return
     */
    @Transactional(readOnly = true)
    public List<StoreRes> getMyStore(Integer memberId) {
        List<Store> response = storeRepository.findBySellerId(memberId);
        return storeMapper.toDtoList(response);
    }

    /**
     * store 조회
     *
     * @param storeId
     * @return
     */
    @Transactional
    public StoreRes getStoreById(Integer storeId) {
        Store store = findStoreWithException(storeId);

        store.incrementViewCount(); // 조회수 증가
        storeRepository.save(store);

        return storeMapper.toDto(store);
    }

    /**
     * store id 로 store 찾기
     *
     * @param storeId
     * @return
     */
    @Transactional(readOnly = true)
    public Store findStoreWithException(Integer storeId) {
        Store store = storeRepository.findById(storeId)
                                     .orElseThrow(() -> new StoreNotFoundException());
        return store;
    }

    /**
     * 스토어 검색
     *
     * @param searchReq 검색 DTO
     * @return
     */
    @Transactional(readOnly = true)
    public List<StoreRes> searchStores(StoreSearchReq searchReq) {
        Pageable pageable = PageRequest.of(searchReq.getPage(), searchReq.getSize(),
                                           getStoreSort(searchReq.getSortType())
        );
        List<Store> stores = storeRepository.findByConditions(searchReq, pageable);
        return storeMapper.toDtoList(stores);
    }

    /**
     * sort 타입별 처리
     *
     * @param sortType
     * @return
     */
    private Sort getStoreSort(StoreSearchReq.SortType sortType) {
        switch(sortType) {
            case VIEWS:
                return Sort.by(Sort.Direction.DESC, "viewCount");
            case RECENT:
                return Sort.by(Sort.Direction.DESC, "createdAt");
            case CLOSING_SOON:
                return Sort.by(Sort.Direction.ASC, "operateEndAt");
            default:
                return Sort.unsorted();
        }
    }

    /**
     * 스토어 업데이트
     *
     * @param storeId
     * @param storeUpdateReq
     * @param images
     * @param memberId
     * @return
     * @throws Exception
     */
    @Transactional
    public StoreRes updateStore(
            Integer storeId,
            StoreUpdateReq storeUpdateReq,
            MultipartFile[] images,
            Integer memberId
    ) throws Exception {
        // 스토어 조회
        Store store = findStoreWithException(storeId);
        // 실제 사용자인지 확인
        basicMemberService.getMemberById(memberId);
        // 본인 소유가 아니면 예외처리
        if(!store.getMember()
                 .getId()
                 .equals(memberId)) {
            throw new StoreIsNotYoursException();
        }
        // 기존의 이미지, 태그, 운영 시간을 삭제하기 전에 명시적으로 컬렉션을 관리
        if(images != null && images.length > 0) {
            store.getImages()
                 .clear();
            for(StoreImage image : store.getImages()) {
                minioUtil.deleteImage(image.getOriginImageUrl());
                minioUtil.deleteImage(image.getThumbnailImageUrl());
            }
        }

        if(storeUpdateReq.getTags() != null && !storeUpdateReq.getTags()
                                                              .isEmpty()) {
            store.getTags()
                 .clear();
        }

        if(storeUpdateReq.getRunningTimes() != null && !storeUpdateReq.getRunningTimes()
                                                                      .isEmpty()) {
            store.getRunningTimes()
                 .clear();
        }

        // Dto -> entity 업데이트
        storeMapper.updateEntityFromDto(storeUpdateReq, store);

        // 새로운 태그, 이미지, 운영 시간 설정
        store.getTags()
             .addAll(storeTagMapper.toEntityList(storeUpdateReq.getTags(), store));
        store.getRunningTimes()
             .addAll(storeRunningTimeMapper.toEntityList(storeUpdateReq.getRunningTimes(), store));

        // 이미지 처리 및 저장
        if(images != null && images.length > 0) {
            List<StoreImage> storeImages = uploadImagesToMinio(images, store);
            store.getImages()
                 .addAll(storeImages);
        }

        // 수정된 스토어 저장
        Store updatedStore = storeRepository.save(store);

        // 반환
        return storeMapper.toDto(updatedStore);
    }

    // 매일 자정에 상태 업데이트
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void updateStoreStatuses() {
        // 모든 스토어에 대해 상태를 업데이트
        for(Store store : storeRepository.findWithoutClosed()) {
            store.updateStatus();
            storeRepository.save(store);
        }
    }

    /**
     * 나에 대한 스토어 검색
     *
     * @param memberId 사용자 ID
     * @param isVisit  방문 / 소유
     * @return list(storeRes)
     */
    @Transactional(readOnly = true)
    public List<?> getStoreAboutMe(
            Integer memberId,
            Boolean isVisit
    ) {
        // 멤버 찾기
        Member member = basicMemberService.getMemberById(memberId);

        // 방문한 스토어라면
        if(isVisit) {
            return storeRepository.findVisitedStoresByMember(memberId);
        } else { // 소유한 스토어라면
            List<Store> stores = storeRepository.findBySellerId(memberId);
            return storeMapper.toDtoList(stores);
        }
    }
}
