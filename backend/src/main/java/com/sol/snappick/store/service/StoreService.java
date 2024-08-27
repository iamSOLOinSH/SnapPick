package com.sol.snappick.store.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sol.snappick.store.dto.StoreCreateReq;
import com.sol.snappick.store.dto.StoreRes;
import com.sol.snappick.store.dto.StoreSearchReq;
import com.sol.snappick.store.dto.StoreUpdateReq;
import com.sol.snappick.store.dto.storeAPI.StoreAPIDataDto;
import com.sol.snappick.store.dto.storeAPI.StoreAPIRes;
import com.sol.snappick.store.entity.Store;
import com.sol.snappick.store.entity.StoreImage;
import com.sol.snappick.store.entity.StoreRunningTime;
import com.sol.snappick.store.entity.StoreTag;
import com.sol.snappick.store.exception.StoreImageLimitExceedException;
import com.sol.snappick.store.exception.StoreNotFoundException;
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

import lombok.RequiredArgsConstructor;

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
	public StoreRes createPopupStore (
		StoreCreateReq storeCreateReq,
		MultipartFile[] images
	) throws Exception {
		if ( images != null && images.length > 3 ) {
			throw new StoreImageLimitExceedException();
		}

		Store storeToCreate = createStoreWithDetails(storeCreateReq);

		// 이미지 처리 및 저장
		if ( images != null ) {
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
	private List<StoreImage> uploadImagesToMinio (
		MultipartFile[] images,
		Store store
	) throws Exception {
		List<StoreImage> storeImages = new ArrayList<>();

		for (MultipartFile image : images) {
			if ( image.isEmpty() ) {
				continue;
			}
			// 실제 이미지 파일 저장 로직은 생략합니다 (예: 파일 시스템, S3, etc.)
			ImageUploadRes imageDto = minioUtil.uploadImageWithThumbnail(BUCKET_NAME,
																		 image); // 이미지 저장 메서드 필요
			if ( imageDto == null ) {
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
	public void postInitData () throws IOException, InterruptedException {
		// API 로 받은 데이터
		StoreAPIRes storeAPIData = popplyHandler.searchStore();
		// 생성 데이터
		for (StoreAPIDataDto data : storeAPIData.getData()) {
			StoreCreateReq storeCreateReq = storeMapper.apiDataToStoreCreateReq(data);

			createStoreWithDetails(storeCreateReq);
		}

	}

	private Store createStoreWithDetails (StoreCreateReq storeCreateReq) {
		// 1. Store 엔티티 생성
		Store storeToCreate = storeMapper.toEntity(storeCreateReq);
		storeToCreate = storeRepository.save(storeToCreate);

		// 2. 태그 처리
		List<StoreTag> storeTags = storeTagMapper.toEntityList(storeCreateReq.getTags(),
															   storeToCreate);
		storeTagRepository.saveAll(storeTags);
		storeToCreate.setTags(storeTags);

		// 이미지 처리
		if ( storeCreateReq.getImages() != null ) {
			List<StoreImage> storeImages = storeImageMapper.toEntityList(storeCreateReq.getImages(),
																		 storeToCreate);
			storeImageRepository.saveAll(storeImages);
			storeToCreate.setImages(storeImages);
		}

		// 3. 운영 시간 처리
		List<StoreRunningTime> storeRunningTimes = storeRunningTimeMapper.toEntityList(storeCreateReq.getRunningTimes(),
																					   storeToCreate);
		storeRunningRepository.saveAll(storeRunningTimes);
		storeToCreate.setRunningTimes(storeRunningTimes);

		return storeToCreate;
	}

	/**
	 * 내 store 모아보기
	 * @param memberId
	 * @return
	 */
	@Transactional (readOnly = true)
	public List<StoreRes> getMyStore (Integer memberId) {
		List<Store> response = storeRepository.findBySellerId(memberId);
		return storeMapper.toDtoList(response);
	}

	/**
	 * store 조회
	 * @param storeId
	 * @return
	 */
	@Transactional
	public StoreRes getStoreById (Integer storeId) {
		Store store = findStoreWithException(storeId);

		store.incrementViewCount(); // 조회수 증가
		storeRepository.save(store);

		return storeMapper.toDto(store);
	}

	/**
	 * store id 로 store 찾기
	 * @param storeId
	 * @return
	 */
	public Store findStoreWithException (Integer storeId) {
		Store store = storeRepository.findById(storeId)
									 .orElseThrow(() -> new StoreNotFoundException());
		return store;
	}

	/**
	 * 스토어 검색
	 * @param searchReq 검색DTO
	 * @return
	 */
	public List<StoreRes> searchStores (StoreSearchReq searchReq) {
		Pageable pageable = PageRequest.of(searchReq.getPage(),
										   searchReq.getSize(),
										   getStoreSort(searchReq.getSortType()));
		List<Store> stores = storeRepository.findByConditions(searchReq,
															  pageable);
		return storeMapper.toDtoList(stores);
	}

	/**
	 * sort 타입별 처리
	 * @param sortType
	 * @return
	 */
	private Sort getStoreSort (StoreSearchReq.SortType sortType) {
		switch (sortType) {
			case VIEWS:
				return Sort.by(Sort.Direction.DESC,
							   "viewCount");
			case RECENT:
				return Sort.by(Sort.Direction.DESC,
							   "createdAt");
			case CLOSING_SOON:
				return Sort.by(Sort.Direction.ASC,
							   "operateEndAt");
			default:
				return Sort.unsorted();
		}
	}

	// TODO : 수정 시 하위테이블 처리 어떻게 할지 고민!
	@Transactional
	public StoreRes updateStore (
		Integer storeId,
		StoreUpdateReq storeUpdateReq,
		MultipartFile[] images
	) throws Exception {
		// 스토어 조회
		Store store = findStoreWithException(storeId);

		if ( images != null && images.length > 0 )
			storeImageRepository.deleteAllByStore(store);
		storeTagRepository.deleteAllByStore(store);
		storeRunningRepository.deleteAllByStore(store);

		// Dto -> entity
		storeMapper.updateEntityFromDto(storeUpdateReq,
										store);

		// 수정된 스토어를 먼저 저장
		Store updatedStore = storeRepository.save(store);

		// 이미지 처리 및 저장
		if ( images != null && images.length > 0 ) {
			List<StoreImage> storeImages = uploadImagesToMinio(images,
															   store);
			storeImageRepository.saveAll(storeImages);
			// 스토어에 이미지 설정 후 다시 저장
			updatedStore.setImages(storeImages);
			storeRepository.save(updatedStore);  // 이미지가 반영된 스토어 저장
		}

		// 반환
		return storeMapper.toDto(updatedStore);
	}
}
