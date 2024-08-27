package com.sol.snappick.member.exception;

public class MemberBadRequestException extends RuntimeException {

    public MemberBadRequestException() {
        super("사용자에 대한 잘못된 요청입니다.");
    }

    public MemberBadRequestException(String message) {
        super(message);
    }
}
