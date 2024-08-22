package com.sol.snappick.util;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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

        String fileName = file.getOriginalFilename();
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
        System.out.println("originImageUrl = " + originImageUrl);
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
}
