package com.sol.snappick.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import org.springframework.stereotype.Component;

@Component
public class QrUtil {

    private final String LOGO_PATH = "static/logo.png";  // 클래스패스 내의 경로로 변경

    public byte[] generateQrCode(
        String text,
        int width,
        int height
    ) throws IOException, WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);  // 높은 오류 수정 수준

        BitMatrix bitMatrix = qrCodeWriter.encode(
            text, BarcodeFormat.QR_CODE, width, height, hints);

        // QR 코드 이미지를 BufferedImage로 변환
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        // 로고 이미지 불러오기
        InputStream logoStream = getClass().getClassLoader()
                                           .getResourceAsStream(LOGO_PATH);
        if (logoStream == null) {
            throw new IOException("Logo file not found: " + LOGO_PATH);
        }
        BufferedImage logoImage = ImageIO.read(logoStream);

        // 로고 이미지 크기를 QR 코드 크기의 1/5로 리사이즈
        int logoWidth = qrImage.getWidth() / 5;
        int logoHeight = qrImage.getHeight() / 5;

        // 로고 이미지 리사이즈
        Image scaledLogo = logoImage.getScaledInstance(logoWidth, logoHeight, Image.SCALE_SMOOTH);
        // 로고를 중앙에 배치
        int x = (qrImage.getWidth() - logoWidth) / 2;
        int y = (qrImage.getHeight() - logoHeight) / 2;

        // QR 코드에 로고 이미지 합성
        Graphics2D g = qrImage.createGraphics();
        g.drawImage(scaledLogo, x, y, null);
        g.dispose();

        // 결과 이미지를 ByteArrayOutputStream에 저장
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }
}