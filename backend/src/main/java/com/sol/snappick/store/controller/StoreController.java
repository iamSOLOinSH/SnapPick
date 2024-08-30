package com.sol.snappick.store.controller;

import com.sol.snappick.store.dto.StoreCreateReq;
import com.sol.snappick.store.dto.StoreRes;
import com.sol.snappick.store.dto.StoreSearchReq;
import com.sol.snappick.store.dto.StoreUpdateReq;
import com.sol.snappick.store.exception.QrInvalidException;
import com.sol.snappick.store.service.StoreService;
import com.sol.snappick.store.service.StoreVisitService;
import com.sol.snappick.store.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "store", description = "스토어 : 팝업 스토어 관리 API")
@RequestMapping("/stores")
public class StoreController {

    public final StoreService storeService;
    public final StoreVisitService storeVisitService;
    private final JwtUtil jwtUtil;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "팝업 스토어 등록", description = """
            팝업 스토어 등록
            \n
            헤더에 토큰 넣어주세요!
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
            | sellerId | integer | 판매자 ID(비워서 주세요) |
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
            @RequestPart(value = "imageFiles", required = false) MultipartFile[] imageFiles,
            Authentication authentication
    ) throws Exception {
        Integer memberId = Integer.valueOf(authentication.getName());
        StoreRes response = storeService.createPopupStore(storeCreateReq, imageFiles, memberId);
        return ResponseEntity.status(HttpStatus.CREATED)
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
            | query | String | 검색 단어 |
            | size | integer | 한 페이지에 표시할 항목 수 (기본값: 10) |
            | page | integer | 페이지 번호 (0부터 시작, 기본값: 0) |
            | sortType | SortType(enum) | 정렬 타입 (조회순: VIEWS, 최근 등록: RECENT, 운영 마감 임박: CLOSING_SOON) |
            \n
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
    @Operation(summary = "스토어 단일 조회", description = "스토어 단일 조회 API.")
    public ResponseEntity<StoreRes> getStore(
            @PathVariable("store_id") Integer storeId
    ) {
        StoreRes response = storeService.getStoreById(storeId);
        return ResponseEntity.ok()
                .body(response);
    }

    @PutMapping(value = "/{store_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "팝업 스토어 수정", description = """
            팝업 스토어 수정
            \n
            헤더에 토큰 넣어주세요!
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
            @RequestPart(value = "images", required = false) MultipartFile[] images,
            Authentication authentication
    ) throws Exception {
        Integer memberId = Integer.valueOf(authentication.getName());
        StoreRes response = storeService.updateStore(storeId, storeUpdateReq, images, memberId);
        return ResponseEntity.ok()
                .body(response);
    }

    @PostMapping("/{store_id}/visit")
    @Operation(summary = "해당 스토어 방문 처리", description = """
            ## input:
            **token** : (string) qr 코드의 값 -> QR로 통해 접근한건지 확인용 \n
            **store_id** : path parameter \n
            header 에 인증코드도 넣어주세요\n
            \n\n
            ## return :
            cart Id (int)
            """)
    public ResponseEntity<Integer> visitStore(
            @PathVariable("store_id") Integer storeId,
            @RequestParam("token") String token,
            Authentication authentication
    ) throws Exception {
        // QR 코드 토큰 검증
        if (!jwtUtil.validateToken(token)) {
            throw new QrInvalidException();
        }
        // 현재 사용자 ID 식별
        Integer memberId = Integer.valueOf(authentication.getName());

        // 방문처리
        Integer cartId = storeVisitService.recordVisit(storeId, memberId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(cartId);
    }

    @GetMapping("/me")
	@Operation(summary = "내가 가진/방문한 스토어 반환", description = """
        이 API는 사용자가 소유하거나 방문한 스토어의 정보를 반환합니다.
        \n
        **헤더에 토큰을 넣어주세요!**
        \n
        <b>Input</b>:
        \n
        | Name | Type  | Description |
        |-----|-----|-------|
        | isVisit | boolean | true일 경우 방문한 스토어 정보 반환, false일 경우 소유한 스토어 정보 반환 |
        \n
        \n
        <b>Output</b>:
        <br>
        **isVisit 값에 따라 두 가지 경우로 나뉩니다.**
        \n
        **1. isVisit = false (내가 소유한 스토어 정보)**
        <br>
        반환 타입: <b>List(StoreRes)</b>
        <br>
        | Name | Type  | Description |
        |-----|-----|-------|
        | id | Integer | 스토어 ID |
        | name | String | 스토어 이름 |
        | description | String | 스토어 설명 |
        | location | String | 스토어 위치 |
        | latitude | Double | 스토어 위치의 위도 |
        | longitude | Double | 스토어 위치의 경도 |
        | operateStartAt | LocalDate | 운영 시작 날짜 |
        | operateEndAt | LocalDate | 운영 종료 날짜 |
        | viewCount | int | 조회 수 |
        | visitCount | int | 방문 수 |
        | sellerId | Integer | 판매자 ID |
        | tags | List(String) | 태그 목록 |
        | images | List(StoreImageDto) | 스토어 이미지 목록 |
        | runningTimes | List(StoreRunningTimeDto) | 운영 시간 목록 |
        \n
        **2. isVisit = true (내가 방문한 스토어 정보)**
        <br>
        반환 타입: <b>List(VisitedStoreRes)</b>
        <br>
        | Name | Type  | Description |
        |-----|-----|-------|
        | storeDetailDto | VisitedStoreDetailDto | 방문한 스토어의 기본 정보 |
        | - storeId | Integer | 스토어 ID |
        | - name | String | 스토어 이름 |
        | - location | String | 스토어 위치 |
        | storeVisitDto | StoreVisitDto | 방문 기록 정보 |
        | - storeVisitId | Integer | 방문 기록 ID |
        | - cartId | Integer | 방문 시 이용한 장바구니 ID |
        | - visitedAt | LocalDateTime | 방문 일시 |
        | cartPurchasedDto | CartPurchasedDto | 장바구니 구매 정보 |
        | - cartId | Integer | 장바구니 ID |
        | - transactionId | Integer | 거래 ID |
        | - purchasedAmount | Long | 구매 금액 |
        """)

	public ResponseEntity<List<?>> getStoreAboutMe(
            @RequestParam("isVisit") Boolean isVisit,
            Authentication authentication
    ) {
        // 현재 사용자 ID 식별
        Integer memberId = Integer.valueOf(authentication.getName());

        List<?> response = storeService.getStoreAboutMe(memberId, isVisit);
        return ResponseEntity.ok()
                .body(response);
    }
}
