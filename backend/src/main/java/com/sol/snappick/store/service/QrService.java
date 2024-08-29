package com.sol.snappick.store.service;

import com.google.zxing.WriterException;
import com.sol.snappick.store.exception.QrGenerateFailException;
import com.sol.snappick.store.util.JwtUtil;
import com.sol.snappick.util.QrUtil;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QrService {

    private final JwtUtil jwtUtil;
    private final QrUtil qrUtil;

    /**
     * QR 코드 생성 메서드
     *
     * @param uuid
     * @param duration
     * @return
     */
    public byte[] generateQrCode(
        Integer id,
        long duration
    ) {
        try {
            String token = jwtUtil.generateToken(String.valueOf(id), duration); // 토큰 생성

            int width = 500;
            int height = 500;

            byte[] qrImage = qrUtil.generateQrCode(token, width, height);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setContentLength(qrImage.length);

            return qrImage;
        } catch (IOException | WriterException e) {
            throw new QrGenerateFailException();
        }
    }
}
