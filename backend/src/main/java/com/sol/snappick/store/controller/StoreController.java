package com.sol.snappick.store.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sol.snappick.store.dto.StoreCreateReq;
import com.sol.snappick.store.dto.StoreRes;
import com.sol.snappick.store.dto.StoreSearchReq;
import com.sol.snappick.store.dto.StoreUpdateReq;
import com.sol.snappick.store.service.StoreService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
public class StoreController {

	public final StoreService storeService;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "팝업 스토어 등록", description = """
			팝업 스토어 등록
			\n
			\n
			<b>Input</b>:
			\n
			<b>StoreCreateReq</b>
			| Name | Type  | Description |
			|-----|-----|-------|
			| name | string | 스토어 이름 |
			| description | string | 스토어 설명 |
			| location | string | 스토어 위치 |
			| latitude | double | 스토어 위치의 위도 |
			| longitude | double | 스토어 위치의 경도 |
			| operateStartAt | LocalDate | 운영 시작 날짜 |
			| operateEndAt | LocalDate | 운영 종료 날짜 |
			| status | StoreStatus | 스토어 상태 (비워서 주면 알아서 계산해서 채워넣습니다) |
			| sellerId | integer | 판매자 ID |
			| tags | list(String) | 태그 목록 |
			| images | list(StoreImageDto) | 스토어 이미지 목록. <b>여기는 비워서 주세요!</b> |
			| runningTimes | list(StoreRunningTimeDto) | 운영 시간 목록 |
			\n
			\n
			<b>StoreStatus</b>
			| Name | Description |
			|-----|-----|
			| PREPARING | 준비중 |
			| OPERATING | 운영중  |
			| CLOSED | 종료 |
			| TEMPORARILY_CLOSED | 임시 종료 | 
			\n
			\n
			<b>StoreRunningTimeDto</b>
			| Name | Type  | Description |
			|-----|-----|-------|
			| dayOfWeek | DayOfWeek(Enum) | MONDAY,TUESDAY,... 영어 대문자! |
			| startTime | string | 시작 시간 ex) 10:00 |
			| endTime | string | 종료 시간 ex) 16:00 |
			<br>
			<br>
			여기에 추가로 images 넣어주시면 됩니다!
			
			<b>Output</b>:
			<br>
			    type: _description_
			
			| Var | Type | Description |
			|-----|-----|-------|
			|  |  |  |
			""")
	public ResponseEntity<StoreRes> createPopupStore(
			@Valid @RequestPart("storeCreateReq") StoreCreateReq storeCreateReq,
			@RequestPart(value = "imageFiles", required = false) MultipartFile[] imageFiles
	) throws Exception {
		StoreRes response = storeService.createPopupStore(storeCreateReq,
														  imageFiles);
		return ResponseEntity.status(HttpStatus.CREATED)
							 .body(response);
	}

	@GetMapping("/me")
	@Operation(summary = "내 스토어 조회", description = "내 스토어를 조회하는 API")
	public ResponseEntity<List<StoreRes>> getMyStores(@RequestParam("member_id") Integer memberId) {
		List<StoreRes> response = storeService.getMyStore(memberId);
		return ResponseEntity.ok()
							 .body(response);
	}

	@PostMapping("/search")
	@Operation(summary = "스토어 검색", description = """
			스토어 검색 요청을 처리합니다.
			\n
			\n
			## Input:
			\n
			### StoreSearchReq
			| Name | Type  | Description |
			|------|-------|-------------|
			| conditions | list(StoreSearchConditionDto) | 검색 조건 리스트 |
			| size | integer | 한 페이지에 표시할 항목 수 (기본값: 10) |
			| page | integer | 페이지 번호 (0부터 시작, 기본값: 0) |
			| sortType | SortType(enum) | 정렬 타입 (조회순: VIEWS, 최근 등록: RECENT, 운영 마감 임박: CLOSING_SOON) |
			\n
			\n
			### StoreSearchConditionDto
			| Name | Type  | Description |
			|------|-------|-------------|
			| field | string | 검색할 컬럼 이름 (예: 'name', 'tag', '등등', '컬럼에 없는거 검색 필요하시면 추가할게요!') |
			| values | list(String) | 검색할 값 리스트 (예: ['로스트아크', '다른 값']) |
			\n
			### 주의 사항 : 현재 name은 like 연산, tag는 스토어 tag 들에 대해 any + 동등비교해서 해당하는게 있으면 찾는 것으로 되어있습니다!
			\n
			<b>SortType Enum Values</b>:
			\n
			| Value | Description |
			|-------|-------------|
			| VIEWS | 조회순으로 정렬 |
			| RECENT | 최근 등록된 순으로 정렬 |
			| CLOSING_SOON | 운영 마감 임박 순으로 정렬 |
			""")
	public ResponseEntity<List<StoreRes>> searchStores(
			@RequestBody StoreSearchReq storeSearchReq
	) {
		List<StoreRes> response = storeService.searchStores(storeSearchReq);
		return ResponseEntity.ok()
							 .body(response);
	}

	@GetMapping("/{store_id}")
	@Operation(summary = "스토어 단일 조회", description = "스토어 단일 조회 API. 응답의 fromQr이 true면 방문한 사용자임 -> 장바구니 활성화")
	public ResponseEntity<StoreRes> getStore(
			@PathVariable("store_id") Integer storeId,
			@RequestParam(value = "fromQr", defaultValue = "false") Boolean fromQr
	) {
		StoreRes response = storeService.getStoreById(storeId);
		response.setFromQr(fromQr);
		return ResponseEntity.ok()
							 .body(response);
	}

	@PutMapping(value = "/{store_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "팝업 스토어 수정", description = """
			팝업 스토어 수정
			\n
			\n
			<b>Input</b>:
			\n
			<b>StoreUpdateReq</b>
			| Name | Type  | Description |
			|-----|-----|-------|
			| name | string | 스토어 이름 |
			| description | string | 스토어 설명 |
			| location | string | 스토어 위치 |
			| latitude | double | 스토어 위치의 위도 |
			| longitude | double | 스토어 위치의 경도 |
			| operateStartAt | LocalDate | 운영 시작 날짜 |
			| operateEndAt | LocalDate | 운영 종료 날짜 |
			| status | StoreStatus | 스토어 상태 (닫은 상태/임시 닫은 상태 일 때만 넣어주시면 될 거 같습니다.) |
			| tags | list(String) | 태그 목록(여기에 값이 들어오면 기존꺼는 삭제합니다 -> 안바뀌면 안주시면됩니다.) |
			| images | list(StoreImageDto) | 스토어 이미지 목록. <b>여기는 비워서 주세요!</b> |
			| runningTimes | list(StoreRunningTimeDto) | 운영 시간 목록(여기에 값이 들어오면 기존꺼는 삭제합니다 -> 안바뀌면 안주시면됩니다.) |
			\n
			\n
			<b>StoreStatus</b>
			| Name | Description |
			|-----|-----|
			| PREPARING | 준비중 |
			| OPERATING | 운영중  |
			| CLOSED | 종료 |
			| TEMPORARILY_CLOSED | 임시 종료 | 
			\n
			\n
			<b>StoreRunningTimeDto</b>
			| Name | Type  | Description |
			|-----|-----|-------|
			| dayOfWeek | DayOfWeek(Enum) | MONDAY,TUESDAY,... 영어 대문자! |
			| startTime | string | 시작 시간 ex) 10:00 |
			| endTime | string | 종료 시간 ex) 16:00 |
			<br>
			<br>
			여기에 추가로 images 넣어주시면 됩니다!
			
			<b>Output</b>:
			<br>
			    type: _description_
			
			| Var | Type | Description |
			|-----|-----|-------|
			|  |  |  |
			""")
	public ResponseEntity<StoreRes> putStore(
			@PathVariable("store_id") Integer storeId,
			@RequestPart("storeUpdateReq") StoreUpdateReq storeUpdateReq,
			@RequestPart(value = "images", required = false) MultipartFile[] images
	) throws Exception {
		StoreRes response = storeService.updateStore(storeId,
													 storeUpdateReq,
													 images);
		return ResponseEntity.ok()
							 .body(response);
	}

	@Hidden
	@PostMapping("/init")
	@Operation(summary = "스토어 API 에서 데이터 가져오는 것! 한 번만 누르기")
	public ResponseEntity<HttpStatus> postInitData() throws Exception {
		storeService.postInitData();
		return ResponseEntity.status(HttpStatus.CREATED)
							 .build();
	}
}
