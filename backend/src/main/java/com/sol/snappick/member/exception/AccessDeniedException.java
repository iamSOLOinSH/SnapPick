package com.sol.snappick.member.exception;

public class AccessDeniedException extends Exception{
    public AccessDeniedException() {
        super("접근 권한이 없습니다.");
    }

    public AccessDeniedException(String message) {
        super(message);
    }
}
