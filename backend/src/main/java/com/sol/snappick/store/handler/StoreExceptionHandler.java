package com.sol.snappick.store.handler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sol.snappick.store.exception.InvalidAttributeException;
import com.sol.snappick.store.exception.InvalidUUIDFormatException;
import com.sol.snappick.store.exception.QrGenerateFailException;
import com.sol.snappick.store.exception.QrInvalidException;
import com.sol.snappick.store.exception.StoreImageLimitExceedException;
import com.sol.snappick.store.exception.StoreIsNotYoursException;
import com.sol.snappick.store.exception.StoreNotFoundException;

@RestControllerAdvice
public class StoreExceptionHandler {

	/**
	 * 스토어 이미지 개수를 초과한
	 *
	 * @param e StoreImageLimitExceedException
	 * @return 에러코드 + 에러 내용
	 */
	@ExceptionHandler(StoreImageLimitExceedException.class)
	public ResponseEntity<String> handleStoreImageLimitExceedException(
			StoreImageLimitExceedException e
	) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							 .body(e.getMessage());
	}

	/**
	 * 해당 store 를 찾지 못했음
	 *
	 * @param e
	 * @return
	 */
	@ExceptionHandler(StoreNotFoundException.class)
	public ResponseEntity<String> handleStoreNotFoundException(StoreNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
							 .body(e.getMessage());
	}

	/**
	 * 검색 시 잘못된 필드 값 쿼리
	 *
	 * @param e
	 * @return
	 */
	@ExceptionHandler(InvalidAttributeException.class)
	public ResponseEntity<String> handleInvalidAttributeException(InvalidAttributeException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							 .body(e.getMessage());
	}

	/**
	 * 파라미터 valid 에 벗어나는 잘못된 값을 넣으면 나오는 에러
	 *
	 * @param ex MethodArgumentNotValidException
	 * @return 에러코드 + 에러설명
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		// 모든 에러를 가져와서 메시지 구성
		List<String> errorMessages = ex.getBindingResult()
									   .getFieldErrors()
									   .stream()
									   .map(fieldError -> String.format("Field '%s': %s",
																		fieldError.getField(),
																		fieldError.getDefaultMessage()))
									   .toList();

		// 에러 메시지들을 JSON 형식의 배열로 반환
		String errorMessageJson = errorMessages.stream()
											   .map(message -> "\"" + message + "\"")
											   .collect(Collectors.joining(", ",
																		   "[",
																		   "]"));

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							 .body(errorMessageJson);
	}

	/**
	 * 큐알이 invalid 함
	 *
	 * @param e QrInvalidException
	 * @return 에러코드 + 에러설명
	 */
	@ExceptionHandler(QrInvalidException.class)
	public ResponseEntity<String> handleQrInvalidException(QrInvalidException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							 .body(e.getMessage());
	}

	/**
	 * 큐알을 만드는데 실패
	 *
	 * @param e QrInvalidException
	 * @return 에러코드 + 에러설명
	 */
	@ExceptionHandler(QrGenerateFailException.class)
	public ResponseEntity<String> handleQrGenerateFailException(QrGenerateFailException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							 .body(e.getMessage());
	}

	/**
	 * 잘못된 UUID 형식
	 * @param e InvalidUUIDFormatException
	 * @return 에러코드 + 에러설명
	 */
	@ExceptionHandler(InvalidUUIDFormatException.class)
	public ResponseEntity<String> handleInvalidUUIDFormatException(InvalidUUIDFormatException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							 .body(e.getMessage());
	}

	/**
	 * 당신의 스토어가 아닌 것을 다룰때
	 * @param e StoreIsNotYoursException
	 * @return 에러코드 + 에러설명
	 */
	@ExceptionHandler(StoreIsNotYoursException.class)
	public ResponseEntity<String> handleStoreIsNotYoursException(StoreIsNotYoursException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							 .body(e.getMessage());
	}
}
