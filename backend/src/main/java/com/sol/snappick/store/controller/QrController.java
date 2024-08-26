package com.sol.snappick.store.controller;


import com.sol.snappick.util.QrUtil;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store/qr")
public class QrController {

    @GetMapping("/{store_id}")
    public ResponseEntity<byte[]> qr(@PathVariable("store_id") String storeId) {
        try {
            String text = "localhost:8080/stores/" + storeId; // QR 코드에 포함할 URL
            int width = 300;  // QR 코드 이미지의 너비
            int height = 300; // QR 코드 이미지의 높이

            byte[] qrImage = QrUtil.generateQrCode(text, width, height);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setContentLength(qrImage.length);

            return ResponseEntity.ok().headers(headers).body(qrImage);
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }
}
