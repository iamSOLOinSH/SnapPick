package com.sol.snappick.member.exception;

public class BasicBadRequestException extends RuntimeException {

    public BasicBadRequestException() {
        super("잘못된 요청입니다.");
    }

    public BasicBadRequestException(String message) {
        super(message);
    }
}
