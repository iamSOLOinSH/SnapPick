package com.sol.snappick.member.exception;

public class BasicNotFoundException extends RuntimeException {

    public BasicNotFoundException() {
        super("찾을 수 없습니다.");
    }

    public BasicNotFoundException(String message) {
        super(message);
    }
}
