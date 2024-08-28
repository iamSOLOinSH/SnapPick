package com.sol.snappick.store.controller;


import com.sol.snappick.store.exception.QrInvalidException;
import com.sol.snappick.store.service.QrService;
import com.sol.snappick.store.util.JwtUtil;
import com.sol.snappick.util.QrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store/qr")
public class QrController {

    private final JwtUtil jwtUtil;
    private final QrUtil qrUtil;
    private final QrService qrService;


    @PostMapping("/{store_uuid}")
    public ResponseEntity<byte[]> qr(
            @PathVariable("store_uuid") String uuid,
            // 기본 3분
            @RequestParam(defaultValue = "180000", required = false, value = "duration") long duration
    ) {
        byte[] response = qrService.generateQrCode(uuid, duration);
        return ResponseEntity.ok()
                             .body(response);
    }

    // QR 코드 검증 엔드포인트
    @GetMapping("/validate")
    public ResponseEntity<String> validateQr(
            @RequestParam("token") String token,
            Authentication authentication
    ) {
        // 현재 사용자 ID 식별
        Integer memberId = Integer.valueOf(authentication.getName());

        // QR 코드 토큰 검증
        if (!jwtUtil.validateToken(token)) {
            throw new QrInvalidException();
        }

        // QR 코드로부터 스토어 ID 받아오기
        String storeId = jwtUtil.getStoreIdFromToken(token);

        // 필요한 로직 수행
        return ResponseEntity.ok("유효한 QR 코드입니다. Store ID: " + storeId);

    }
}
