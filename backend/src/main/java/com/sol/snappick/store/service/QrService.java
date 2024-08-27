package com.sol.snappick.store.service;

import com.sol.snappick.store.exception.QrGenerateFailException;
import com.sol.snappick.store.util.JwtUtil;
import com.sol.snappick.util.QrUtil;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QrService {

    private final JwtUtil jwtUtil;
    private final QrUtil qrUtil;

    @Value("${app.base-url}")
    private String baseUrl;

    /**
     * QR 코드 생성 메서드
     *
     * @param uuid
     * @param duration
     * @return
     */
    public byte[] generateQrCode(
            String uuid,
            long duration
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

            return qrImage;
        } catch (IOException e) {
            throw new QrGenerateFailException();
        }
    }
}
