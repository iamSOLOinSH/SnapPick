package com.sol.snappick.store.handler;

import com.sol.snappick.store.exception.StoreImageLimitExceedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
}
