package com.sol.snappick.store.controller;

import com.sol.snappick.store.dto.StoreRes;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stores")
public class StoreController {

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "팝업 스토어 등록", description = """
        팝업 스토어 등록 API
        """)
    public ResponseEntity<StoreRes> createPopupStore() {
        return null;
    }
}
