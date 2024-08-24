package com.sol.snappick.store.service;

import com.sol.snappick.store.dto.StoreCreateReq;
import com.sol.snappick.store.dto.StoreRes;
import com.sol.snappick.store.dto.storeAPI.StoreAPIDataDto;
import com.sol.snappick.store.dto.storeAPI.StoreAPIRes;
import com.sol.snappick.store.entity.Store;
import com.sol.snappick.store.entity.StoreImage;
import com.sol.snappick.store.entity.StoreRunningTime;
import com.sol.snappick.store.entity.StoreTag;
import com.sol.snappick.store.exception.StoreImageLimitExceedException;
import com.sol.snappick.store.mapper.StoreImageMapper;
import com.sol.snappick.store.mapper.StoreMapper;
import com.sol.snappick.store.mapper.StoreRunningTimeMapper;
import com.sol.snappick.store.mapper.StoreTagMapper;
import com.sol.snappick.store.repository.StoreImageRepository;
import com.sol.snappick.store.repository.StoreRepository;
import com.sol.snappick.store.repository.StoreRunningTimeRepository;
import com.sol.snappick.store.repository.StoreTagRepository;
import com.sol.snappick.util.ImageUploadRes;
import com.sol.snappick.util.MinioUtil;
import com.sol.snappick.util.PopplyHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

    public final PopplyHandler popplyHandler;

    // MINIO 버킷 이름
    private final String BUCKET_NAME = "snappick-store";
    private final MinioUtil minioUtil;

    /**
     * pop up store create 서비스 로직
     *
     * @param storeCreateReq
     * @param images
     * @return
     * @throws Exception
     */
    @Transactional
    public StoreRes createPopupStore(
        StoreCreateReq storeCreateReq,
        MultipartFile[] images
    ) throws Exception {
        if (images != null && images.length > 3) {
            throw new StoreImageLimitExceedException();
        }
        // 1. Store 엔티티 생성
        Store storeToCreate = storeMapper.toEntity(storeCreateReq);
        storeToCreate = storeRepository.save(storeToCreate);

        // 2. 이미지 처리 및 저장
        if (images != null) {
            List<StoreImage> storeImages = uploadImagesToMinio(images, storeToCreate);
            storeImageRepository.saveAll(storeImages);
            storeToCreate.setImages(storeImages);
        }

        // 3. 태그 처리
        List<StoreTag> storeTags = storeTagMapper.toEntityList(
            storeCreateReq.getTags(), storeToCreate);
        storeTagRepository.saveAll(storeTags);

        storeToCreate.setTags(storeTags);

        // 4. 운영 시간 처리
        List<StoreRunningTime> storeRunningTimes = storeRunningTimeMapper.toEntityList(
            storeCreateReq.getRunningTimes(), storeToCreate);
        storeRunningRepository.saveAll(storeRunningTimes);

        storeToCreate.setRunningTimes(storeRunningTimes);

        // 5. 최종 DTO로 변환하여 반환
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

        for (MultipartFile image : images) {
            if (image.isEmpty()) {
                continue;
            }
            // 실제 이미지 파일 저장 로직은 생략합니다 (예: 파일 시스템, S3, etc.)
            ImageUploadRes imageDto = minioUtil.uploadImageWithThumbnail(
                BUCKET_NAME, image); // 이미지 저장 메서드 필요
            if (imageDto == null) {
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
     * api 를 사용하여 초기 팝업스토어 정보 넣기
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Transactional
    public void postInitData() throws IOException, InterruptedException {
        // API 로 받은 데이터
        StoreAPIRes storeAPIData = popplyHandler.searchStore();
        // 생성 데이터
        for (StoreAPIDataDto data : storeAPIData.getData()) {
            StoreCreateReq storeCreateReq = storeMapper.apiDataToStoreCreateReq(data);
            //            createDtos.add(storeCreateReq);
            // 1. Store 엔티티 생성
            Store storeToCreate = storeMapper.toEntity(storeCreateReq);
            storeToCreate = storeRepository.save(storeToCreate);

            // 2. 이미지 처리 및 저장
            if (storeCreateReq.getImages() != null) {
                List<StoreImage> storeImages = storeImageMapper.toEntityList(
                    storeCreateReq.getImages(), storeToCreate);
                storeImageRepository.saveAll(storeImages);
                storeToCreate.setImages(storeImages);
            }

            // 3. 태그 처리
            List<StoreTag> storeTags = storeTagMapper.toEntityList(
                storeCreateReq.getTags(), storeToCreate);
            storeTagRepository.saveAll(storeTags);

            storeToCreate.setTags(storeTags);

            // 4. 운영 시간 처리
            List<StoreRunningTime> storeRunningTimes = storeRunningTimeMapper.toEntityList(
                storeCreateReq.getRunningTimes(), storeToCreate);
            storeRunningRepository.saveAll(storeRunningTimes);

            storeToCreate.setRunningTimes(storeRunningTimes);
        }

    }
}
