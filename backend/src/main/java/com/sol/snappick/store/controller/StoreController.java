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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "팝업 스토어 등록", description = "팝업 스토어 등록 API")
    public ResponseEntity<StoreRes> createPopupStore(
        @RequestPart("storeCreateReq") StoreCreateReq storeCreateReq,
        @RequestPart(value = "images", required = false) MultipartFile[] images
    ) throws Exception {
        StoreRes response = storeService.createPopupStore(storeCreateReq, images);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(response);
    }
}
