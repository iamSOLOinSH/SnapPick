package com.sol.snappick.util.minio;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import net.coobird.thumbnailator.Thumbnails;

import io.jsonwebtoken.io.IOException;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.SetBucketPolicyArgs;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MinioUtil {

	private final Integer THUMBNAIL_WIDTH = 200;
	private final Integer THUMBNAIL_HEIGHT = 200;
	private final MinioClient minioClient;
	private final Object bucketLock = new Object();  // 버킷 생성 동기화를 위한 객체

	@Value ("${minio.endpoint}")
	private String minioUrl;

	public static String generateUniqueFileName (String originalFileName) {
		// UUID 생성
		String uuid = UUID.randomUUID()
						  .toString();

		// 확장자 추출
		String extension = "";
		int dotIndex = originalFileName.lastIndexOf('.');
		if ( dotIndex > 0 ) {
			extension = originalFileName.substring(dotIndex);
		}

		// UUID 와 원본 파일 이름을 결합하여 고유한 파일 이름 생성
		return uuid + extension;
	}

	// 퍼블릭 버킷 정책 생성
	private static String getPublicBucketPolicy (String bucketName) {
		return "{\n" + "    \"Version\": \"2012-10-17\",\n" + "    \"Statement\": [\n" + "        {\n"
			   + "            \"Effect\": \"Allow\",\n" + "            \"Principal\": \"*\",\n"
			   + "            \"Action\": [\n" + "                \"s3:GetObject\"\n" + "            ],\n"
			   + "            \"Resource\": [\n" + "                \"arn:aws:s3:::" + bucketName + "/*\"\n"
			   + "            ]\n" + "        }\n" + "    ]\n" + "}";
	}

	public boolean deleteImage (String imageUrl) throws Exception {
		try {
			URL url = new URL(imageUrl);
			String path = url.getPath();
			// path는 일반적으로 "/bucketName/fileName" 형식입니다.
			String[] parts = path.split("/",
										3);
			if ( parts.length < 3 ) {
				throw new IllegalArgumentException("Invalid image URL format");
			}
			String bucketName = parts[1];
			String fileName = parts[2];

			return deleteFromMinio(bucketName,
								   fileName);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("Invalid image URL",
											   e);
		}
	}

	private boolean deleteFromMinio (
			String bucketName,
			String fileName
	) throws Exception {
		try {
			minioClient.removeObject(RemoveObjectArgs.builder()
													 .bucket(bucketName)
													 .object(fileName)
													 .build());
			return true;
		} catch (MinioException e) {
			throw new MinioException("Error deleting file from MinIO: " + e.getMessage());
		}
	}

	public ImageUploadRes uploadImageWithThumbnail (
			String bucketName,
			MultipartFile file
	) throws Exception {
		// 버킷이 존재하는지 확인
		boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
																 .bucket(bucketName)
																 .build());
		if ( !found ) {
			// 버킷이 존재하지 않으면 생성
			minioClient.makeBucket(MakeBucketArgs.builder()
												 .bucket(bucketName)
												 .build());

			// 버킷을 퍼블릭으로 설정
			minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()
														   .bucket(bucketName)
														   .config(getPublicBucketPolicy(bucketName))
														   .build());
		}

		String originFileName = file.getOriginalFilename();
		if ( originFileName == null ) {
			throw new ImageNameNullException();
		}
		String fileName = generateUniqueFileName(originFileName);

		InputStream inputStream = file.getInputStream();

		// 업로드할 파일의 썸네일 생성
		ByteArrayOutputStream thumbnailOutputStream = new ByteArrayOutputStream();
		Thumbnails.of(inputStream)
				  .size(THUMBNAIL_WIDTH,
						THUMBNAIL_HEIGHT)
				  .outputFormat("jpg")
				  .toOutputStream(thumbnailOutputStream);

		// 원본 이미지 업로드
		String originImageUrl = uploadToMinio(bucketName,
											  fileName,
											  file.getContentType(),
											  file.getInputStream());

		// 썸네일 이미지 업로드
		String thumbnailFileName = "thumbnail_" + fileName;
		String thumbnailImageUrl = uploadToMinio(bucketName,
												 thumbnailFileName,
												 "image/jpeg",
												 new ByteArrayInputStream(thumbnailOutputStream.toByteArray()));

		return ImageUploadRes.builder()
							 .originImageUrl(originImageUrl)
							 .thumbnailImageUrl(thumbnailImageUrl)
							 .build();
	}

	/**
	 * image url에서 다운로드
	 * @param bucketName
	 * @param imageUrl
	 * @return
	 * @throws Exception
	 */
	public ImageUploadRes uploadImageFromUrlWithThumbnail (
			String bucketName,
			String imageUrl
	) throws Exception {

		try {
			// 1. 이미지 URL에서 이미지 스트림 다운로드
			InputStream imageStream = downloadImageFromUrl(imageUrl);

			// 2. 파일명 생성
			String fileName = generateUniqueFileName(new URL(imageUrl).getPath());

			// 3. 버킷이 존재하는지 확인
			boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
																	 .bucket(bucketName)
																	 .build());
			if ( !found ) {
				synchronized (bucketLock) {
					// 이중 체크: 다른 스레드가 이미 버킷을 생성했는지 다시 확인
					found = minioClient.bucketExists(BucketExistsArgs.builder()
																	 .bucket(bucketName)
																	 .build());
					if ( !found ) {
						// 버킷이 존재하지 않으면 생성
						minioClient.makeBucket(MakeBucketArgs.builder()
															 .bucket(bucketName)
															 .build());

						// 버킷을 퍼블릭으로 설정
						minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()
																	   .bucket(bucketName)
																	   .config(getPublicBucketPolicy(bucketName))
																	   .build());
					}
				}
			}

			// 4. 썸네일 생성
			ByteArrayOutputStream thumbnailOutputStream = new ByteArrayOutputStream();
			try {
				BufferedImage bufferedImage = ImageIO.read(imageStream);
				if ( bufferedImage == null ) {
					throw new IOException("Failed to read the image from the provided URL");
				}

				Thumbnails.of(bufferedImage)
						  .size(THUMBNAIL_WIDTH,
								THUMBNAIL_HEIGHT)
						  .outputFormat("jpg")
						  .toOutputStream(thumbnailOutputStream);

				// 원본 이미지와 썸네일 이미지 업로드 로직 추가
			} catch (IOException e) {
				e.printStackTrace();
			}

			// 원본 이미지 스트림 다시 생성
			InputStream originalImageStream = downloadImageFromUrl(imageUrl);

			// 5. 원본 이미지 MinIO에 업로드
			String originImageUrl = uploadToMinio(bucketName,
												  fileName,
												  "image/jpeg",
												  originalImageStream);

			// 6. 썸네일 이미지 MinIO에 업로드
			String thumbnailFileName = "thumbnail_" + fileName;
			String thumbnailImageUrl = uploadToMinio(bucketName,
													 thumbnailFileName,
													 "image/jpeg",
													 new ByteArrayInputStream(thumbnailOutputStream.toByteArray()));

			// 7. 업로드 결과 반환
			return ImageUploadRes.builder()
								 .originImageUrl(originImageUrl)
								 .thumbnailImageUrl(thumbnailImageUrl)
								 .build();

		} catch (Exception e) {
			throw new Exception("Failed to upload image from URL with thumbnail",
								e);
		}
	}

	public ImageUploadRes uploadImage (
			String bucketName,
			MultipartFile file
	) throws Exception {
		String fileName = file.getOriginalFilename();
		InputStream inputStream = file.getInputStream();

		// 원본 이미지 업로드
		String originImageUrl = uploadToMinio(bucketName,
											  fileName,
											  file.getContentType(),
											  file.getInputStream());

		return ImageUploadRes.builder()
							 .originImageUrl(originImageUrl)
							 .build();
	}

	/**
	 * minio 에 업로드
	 * @param bucketName
	 * @param fileName
	 * @param contentType
	 * @param inputStream
	 * @return
	 * @throws Exception
	 */
	private String uploadToMinio (
			String bucketName,
			String fileName,
			String contentType,
			InputStream inputStream
	) throws Exception {
		try {
			minioClient.putObject(PutObjectArgs.builder()
											   .bucket(bucketName)
											   .object(fileName)
											   .stream(inputStream,
													   inputStream.available(),
													   -1)
											   .contentType(contentType)
											   .build());
		} catch (MinioException e) {
			throw new MinioException(e.getMessage());
		}

		// 퍼블릭 URL 반환 (정적 URL)
		return generatePublicUrl(bucketName,
								 fileName);
	}

	/**
	 * public url 만들기
	 *
	 * @param bucketName
	 * @param fileName
	 * @return
	 */
	private String generatePublicUrl (
			String bucketName,
			String fileName
	) {
		return minioUrl + "/" + bucketName + "/" + fileName;
	}

	/**
	 * url에서 image 다운로드
	 * @param imageUrl
	 * @return
	 * @throws Exception
	 */
	private InputStream downloadImageFromUrl (String imageUrl) throws Exception {
		// 다운로드할 url
		URL url = new URL(imageUrl);
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setRequestMethod("GET");
		connection.connect();
		// download error
		if ( connection.getResponseCode() != HttpURLConnection.HTTP_OK ) {
			throw new RuntimeException("Failed to download image: " + connection.getResponseCode());
		}

		return connection.getInputStream();
	}
}
