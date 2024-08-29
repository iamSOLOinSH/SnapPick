package com.sol.snappick.store.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sol.snappick.store.dto.StoreCreateReq;
import com.sol.snappick.store.dto.StoreImageDto;
import com.sol.snappick.store.dto.StoreRes;
import com.sol.snappick.store.dto.StoreSearchReq;
import com.sol.snappick.store.dto.StoreUpdateReq;
import com.sol.snappick.store.dto.storeAPI.StoreAPIDataDto;
import com.sol.snappick.store.dto.storeAPI.StoreAPIRes;
import com.sol.snappick.store.entity.Store;
import com.sol.snappick.store.entity.StoreImage;
import com.sol.snappick.store.exception.InvalidUUIDFormatException;
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
import com.sol.snappick.util.StoreAPIHandler;
import com.sol.snappick.util.minio.ImageUploadRes;
import com.sol.snappick.util.minio.MinioUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

	public final StoreAPIHandler storeAPIHandler;

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
		if(images != null && images.length > 3) {
			throw new StoreImageLimitExceedException();
		}

		Store storeToCreate = createStoreWithDetails(storeCreateReq);
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
																		 image); // 이미지 저장 메서드 필요
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
	 * api 를 사용하여 초기 팝업스토어 정보 넣기
	 *
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Transactional
	public void postInitData() throws IOException, InterruptedException {
		// API 로 받은 데이터
		StoreAPIRes storeAPIData = storeAPIHandler.searchStore();
		List<Store> toCreateStores = new ArrayList<>();

		// 병렬 처리를 위한 스레드 풀 설정
		ExecutorService executorService = Executors.newFixedThreadPool(10);

		// 생성 데이터
		for(StoreAPIDataDto data : storeAPIData.getData()) {
			StoreCreateReq storeCreateReq = storeMapper.apiDataToStoreCreateReq(data);

			// 이미지 처리 및 MinIO에 업로드
			List<StoreImageDto> images = storeCreateReq.getImages();
			int end = Math.min(images.size(),
							   10);  // 10개 이하로 가져오기
			images = images.subList(0,
									end);

			if(images != null && !images.isEmpty()) {
				// 비동기 처리 시작
				List<CompletableFuture<StoreImageDto>> futures = images.stream()
																	   .map(imageDto -> CompletableFuture.supplyAsync(() -> {
																														  try {
																															  ImageUploadRes uploadRes = minioUtil.uploadImageFromUrlWithThumbnail(BUCKET_NAME,
																																																   imageDto.getOriginImageUrl());

																															  return StoreImageDto.builder()
																																				  .originImageUrl(uploadRes.getOriginImageUrl())
																																				  .thumbnailImageUrl(uploadRes.getThumbnailImageUrl())
																																				  .build();
																														  } catch(Exception e) {
																															  // 이미지 업로드 실패 시 예외 처리 로직 추가 가능
																															  e.printStackTrace();
																															  return null;
																														  }
																													  },
																													  executorService))
																	   .toList();

				// CompletableFuture 결과 모으기
				List<StoreImageDto> storeImages = futures.stream()
														 .map(CompletableFuture::join)
														 .filter(Objects::nonNull)
														 .toList();

				// Store 엔티티에 업로드된 이미지 설정
				storeCreateReq.setImages(storeImages);
			}
			// Store 엔티티 생성 및 태그, 이미지, 운영 시간 설정
			toCreateStores.add(createStoreWithDetails(storeCreateReq));
		}

		storeRepository.saveAll(toCreateStores);
		executorService.shutdown();
	}

	/**
	 * store 생성
	 * @param storeCreateReq
	 * @return
	 */
	private Store createStoreWithDetails(StoreCreateReq storeCreateReq) {
		// Store 엔티티 생성 및 태그, 이미지, 운영 시간 설정
		Store storeToCreate = storeMapper.toEntity(storeCreateReq);
		storeToCreate.setTags(storeTagMapper.toEntityList(storeCreateReq.getTags(),
														  storeToCreate));
		storeToCreate.setImages(storeImageMapper.toEntityList(storeCreateReq.getImages(),
															  storeToCreate));
		storeToCreate.setRunningTimes(storeRunningTimeMapper.toEntityList(storeCreateReq.getRunningTimes(),
																		  storeToCreate));

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
	 * @param searchReq 검색DTO
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<StoreRes> searchStores(StoreSearchReq searchReq) {
		Pageable pageable = PageRequest.of(searchReq.getPage(),
										   searchReq.getSize(),
										   getStoreSort(searchReq.getSortType()));
		List<Store> stores = storeRepository.findByConditions(searchReq,
															  pageable);
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

	/**
	 * 스토어 업데이트
	 * @param storeId
	 * @param storeUpdateReq
	 * @param images
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public StoreRes updateStore(
			Integer storeId,
			StoreUpdateReq storeUpdateReq,
			MultipartFile[] images
	) throws Exception {
		// 스토어 조회
		Store store = findStoreWithException(storeId);

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
		storeMapper.updateEntityFromDto(storeUpdateReq,
										store);

		// 새로운 태그, 이미지, 운영 시간 설정
		store.getTags()
			 .addAll(storeTagMapper.toEntityList(storeUpdateReq.getTags(),
												 store));
		store.getRunningTimes()
			 .addAll(storeRunningTimeMapper.toEntityList(storeUpdateReq.getRunningTimes(),
														 store));

		// 이미지 처리 및 저장
		if(images != null && images.length > 0) {
			List<StoreImage> storeImages = uploadImagesToMinio(images,
															   store);
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
	 * storeUUID 로 storeID 받기
	 * @param storeUUID
	 * @return
	 */
	public Integer getStoreIdByUUID(String storeUUID) {
		try {
			// UUID 변환 시 발생할 수 있는 IllegalArgumentException 처리
			UUID uuid = UUID.fromString(storeUUID);
			Integer storeId = storeRepository.findByUUID(uuid);

			if(storeId == null) {
				throw new StoreNotFoundException();
			}

			return storeId;
		} catch(IllegalArgumentException e) {
			// 잘못된 UUID 형식인 경우 처리
			throw new InvalidUUIDFormatException();
		}
	}
}
