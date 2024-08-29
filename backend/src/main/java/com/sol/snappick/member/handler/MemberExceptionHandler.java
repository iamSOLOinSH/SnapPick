package com.sol.snappick.member.handler;

import com.sol.snappick.member.exception.BasicBadRequestException;
import com.sol.snappick.member.exception.BasicNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MemberExceptionHandler {

    @ExceptionHandler(BasicBadRequestException.class)
    public ResponseEntity<String> handleMemberNotFoundException(BasicBadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(BasicNotFoundException.class)
    public ResponseEntity<String> handleAccountNotFoundException(BasicNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
}
