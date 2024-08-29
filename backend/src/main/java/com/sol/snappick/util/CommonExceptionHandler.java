package com.sol.snappick.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sol.snappick.util.minio.ImageDownloadFailException;
import com.sol.snappick.util.minio.ImageNameNullException;

@RestControllerAdvice
public class CommonExceptionHandler {
	@ExceptionHandler (ImageDownloadFailException.class)
	public ResponseEntity<String> imageDownloadFail (ImageDownloadFailException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							 .body(e.getMessage());
	}

	@ExceptionHandler (ImageNameNullException.class)
	public ResponseEntity<String> imageDownloadFail (ImageNameNullException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							 .body(e.getMessage());
	}
}
