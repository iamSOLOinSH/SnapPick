package com.sol.snappick.store.controller;

import com.sol.snappick.store.dto.StoreRes;
import com.sol.snappick.store.exception.QrInvalidException;
import com.sol.snappick.store.service.QrService;
import com.sol.snappick.store.service.StoreService;
import com.sol.snappick.store.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
public class QrController {

    private final JwtUtil jwtUtil;
    private final QrService qrService;
    private final StoreService storeService;

    @PostMapping("/{store_id}/qr")
    @Operation(summary = "QR 코드 생성", description = "store의 id를 넣어서 해당 스토어로 입장할 수 있는 QR을 생성하는 API. duration(유효시간) 은 기본 3분")
    public ResponseEntity<byte[]> makeQr(
        @PathVariable("store_id") Integer storeId,
        // 기본 3분
        @RequestParam(defaultValue = "180000", required = false, value = "duration") long duration
    ) {
        byte[] response = qrService.generateQrCode(storeId, duration);
        return ResponseEntity.ok()
                             .contentType(MediaType.IMAGE_PNG)
                             .body(response);
    }

    // QR 코드 검증 엔드포인트
    @GetMapping("/qr/validate")
    @Operation(summary = "QR 코드 검증 및 해당 스토어 정보 return", description = "QR코드를 검증하고, 스토어 정보를 반환합니다.")
    public ResponseEntity<StoreRes> validateQrAndVisitStore(
        @RequestParam("token") String token
    ) {
        // QR 코드 토큰 검증
        if (!jwtUtil.validateToken(token)) {
            throw new QrInvalidException();
        }

        // QR 코드로부터 스토어 ID 받아오기
        Integer storeId = Integer.valueOf(jwtUtil.getStoreIdFromToken(token));

        // 스토어 조회
        StoreRes response = storeService.getStoreById(storeId);

        return ResponseEntity.ok()
                             .body(response);

    }
}
