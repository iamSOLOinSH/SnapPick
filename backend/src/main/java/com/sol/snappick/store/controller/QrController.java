package com.sol.snappick.store.controller;


import com.sol.snappick.store.util.JwtUtil;
import com.sol.snappick.util.QrUtil;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store/qr")
public class QrController {

    private final JwtUtil jwtUtil;
    private final QrUtil qrUtil;
    @Value("${app.base-url}")
    private String baseUrl;

    @GetMapping("/{store_uuid}")
    public ResponseEntity<byte[]> qr(
            @PathVariable("store_uuid") String uuid,
            // 기본 3분
            @RequestParam(defaultValue = "180_000", required = false) long duration
    ) {
        try {
            String token = jwtUtil.generateToken(uuid, duration); // 토큰 생성
            String url = baseUrl + "/store/qr/validate?token=" + token; // QR 코드에 포함될 URL

            int width = 300;
            int height = 300;

            byte[] qrImage = qrUtil.generateQrCode(url, width, height);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setContentLength(qrImage.length);

            return ResponseEntity.ok()
                                 .headers(headers)
                                 .body(qrImage);
        } catch (IOException e) {
            return ResponseEntity.status(500)
                                 .build();
        }
    }

    // QR 코드 검증 엔드포인트
    @GetMapping("/validate")
    public ResponseEntity<String> validateQr(@RequestParam String token) {
        if (jwtUtil.validateToken(token)) {
            String storeId = jwtUtil.getStoreIdFromToken(token);

            // 필요한 로직 수행
            return ResponseEntity.ok("유효한 QR 코드입니다. Store ID: " + storeId);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("유효하지 않거나 만료된 QR 코드입니다.");
        }
    }
}
