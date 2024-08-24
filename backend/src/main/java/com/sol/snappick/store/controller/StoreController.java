package com.sol.snappick.store.controller;

import com.sol.snappick.store.dto.StoreCreateReq;
import com.sol.snappick.store.dto.StoreRes;
import com.sol.snappick.store.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
        | sellerId | integer | 판매자 ID |
        | tags | list(String) | 태그 목록 |
        | images | list(StoreImageDto) | 스토어 이미지 목록. <b>여기는 비워서 주세요!</b> |
        | runningTimes | list(StoreRunningTimeDto) | 운영 시간 목록 |
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
        @RequestPart("storeCreateReq") StoreCreateReq storeCreateReq,
        @RequestPart(value = "images", required = false) MultipartFile[] images
    ) throws Exception {
        StoreRes response = storeService.createPopupStore(storeCreateReq, images);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(response);
    }

    @PostMapping("/init")
    @Operation(summary = "스토어 API 에서 데이터 가져오는 것! 한 번만 누르기")
    public ResponseEntity<HttpStatus> postInitData() throws Exception {
        storeService.postInitData();
        return ResponseEntity.status(HttpStatus.CREATED)
                             .build();
    }
}
