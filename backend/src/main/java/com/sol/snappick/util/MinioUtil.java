package com.sol.snappick.util;

import io.minio.*;
import io.minio.errors.MinioException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.UUID;
import java.net.MalformedURLException;
import java.net.URL;

import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class MinioUtil {

    private final Integer THUMBNAIL_WIDTH = 200;
    private final Integer THUMBNAIL_HEIGHT = 200;

    private final MinioClient minioClient;

    public ImageUploadRes uploadImageWithThumbnail(
        String bucketName,
        MultipartFile file
    ) throws Exception {
        // 버킷이 존재하는지 확인
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                                                                 .bucket(bucketName)
                                                                 .build());
        if (!found) {
            // 버킷이 존재하지 않으면 생성
            minioClient.makeBucket(MakeBucketArgs.builder()
                                                 .bucket(bucketName)
                                                 .build());
        }

        String originFileName = file.getOriginalFilename();
        // filename 이 비어있으면 에러
        if(originFileName == null) {
            throw new ImageNameNullException();
        }
        String fileName = generateUniqueFileName(originFileName);

        InputStream inputStream = file.getInputStream();

        // 업로드할 파일의 썸네일 생성
        ByteArrayOutputStream thumbnailOutputStream = new ByteArrayOutputStream();
        Thumbnails.of(inputStream)
                  .size(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT)
                  .outputFormat("jpg")
                  .toOutputStream(thumbnailOutputStream);

        // 원본 이미지 업로드
        String originImageUrl = uploadToMinio(
            bucketName, fileName, file.getContentType(), file.getInputStream());

        // 썸네일 이미지 업로드
        String thumbnailFileName = "thumbnail_" + fileName;
        String thumbnailImageUrl = uploadToMinio(
            bucketName, thumbnailFileName, "image/jpeg", new ByteArrayInputStream(
                thumbnailOutputStream.toByteArray()));

        return ImageUploadRes.builder()
                             .originImageUrl(originImageUrl)
                             .thumbnailImageUrl(thumbnailImageUrl)
                             .build();
    }

    public ImageUploadRes uploadImage(
        String bucketName,
        MultipartFile file
    ) throws Exception {
        String fileName = file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();

        // 원본 이미지 업로드
        String originImageUrl = uploadToMinio(
            bucketName, fileName, file.getContentType(), file.getInputStream());

        return ImageUploadRes.builder()
                             .originImageUrl(originImageUrl)
                             .build();
    }

    private String uploadToMinio(
        String bucketName,
        String fileName,
        String contentType,
        InputStream inputStream
    ) throws Exception {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                                               .bucket(bucketName)
                                               .object(fileName)
                                               .stream(inputStream, inputStream.available(), -1)
                                               .contentType(contentType)
                                               .build());
        } catch (MinioException e) {
            throw new MinioException(e.getMessage());
        }
        return minioClient.getPresignedObjectUrl(io.minio.GetPresignedObjectUrlArgs.builder()
                                                                                   .method(
                                                                                       Method.GET)
                                                                                   .bucket(
                                                                                       bucketName)
                                                                                   .object(fileName)
                                                                                   .build());
    }

    public boolean deleteImage(String imageUrl) throws Exception {
        try {
            URL url = new URL(imageUrl);
            String path = url.getPath();
            // path는 일반적으로 "/bucketName/fileName" 형식입니다.
            String[] parts = path.split("/", 3);
            if (parts.length < 3) {
                throw new IllegalArgumentException("Invalid image URL format");
            }
            String bucketName = parts[1];
            String fileName = parts[2];

            return deleteFromMinio(bucketName, fileName);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid image URL", e);
        }
    }

    private boolean deleteFromMinio(String bucketName, String fileName) throws Exception {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
            return true;
        } catch (MinioException e) {
            throw new MinioException("Error deleting file from MinIO: " + e.getMessage());
        }
    }

    public static String generateUniqueFileName(String originalFileName) {
        // UUID 생성
        String uuid = UUID.randomUUID().toString();

        // 확장자 추출
        String extension = "";
        int dotIndex = originalFileName.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalFileName.substring(dotIndex);
        }

        // UUID와 원본 파일 이름을 결합하여 고유한 파일 이름 생성
        return uuid + extension;
    }
}
