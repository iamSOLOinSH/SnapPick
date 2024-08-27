package com.sol.snappick.member.handler;

import com.sol.snappick.member.exception.MemberBadRequestException;
import com.sol.snappick.member.exception.MemberNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MemberExceptionHandler {

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<String> handleMemberNotFoundException(MemberNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(e.getMessage());
    }

    @ExceptionHandler(MemberBadRequestException.class)
    public ResponseEntity<String> handleMemberBadRequestException(MemberBadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(e.getMessage());
    }
}
