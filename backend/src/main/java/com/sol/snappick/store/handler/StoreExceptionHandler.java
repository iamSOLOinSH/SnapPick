package com.sol.snappick.store.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sol.snappick.store.exception.InvalidAttributeException;
import com.sol.snappick.store.exception.StoreImageLimitExceedException;
import com.sol.snappick.store.exception.StoreNotFoundException;

@RestControllerAdvice
public class StoreExceptionHandler {

	/**
	 * 스토어 이미지 개수를 초과한
	 *
	 * @param e StoreImageLimitExceedException
	 * @return 에러코드 + 에러 내용
	 */
	@ExceptionHandler (StoreImageLimitExceedException.class)
	public ResponseEntity<String> handleStoreImageLimitExceedException (
		StoreImageLimitExceedException e
	) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							 .body(e.getMessage());
	}

	/**
	 * 해당 store 를 찾지 못했음
	 * @param e
	 * @return
	 */
	@ExceptionHandler (StoreNotFoundException.class)
	public ResponseEntity<String> handleStoreNotFoundException (StoreNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
							 .body(e.getMessage());
	}

	/**
	 * 검색 시 잘못된 필드 값 쿼리
	 * @param e
	 * @return
	 */
	@ExceptionHandler (InvalidAttributeException.class)
	public ResponseEntity<String> handleInvalidAttributeException (InvalidAttributeException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							 .body(e.getMessage());
	}
}
