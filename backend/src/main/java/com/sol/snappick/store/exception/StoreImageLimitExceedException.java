package com.sol.snappick.store.exception;

public class StoreImageLimitExceedException extends RuntimeException {

    /**
     * 이미지 개수 세장 넘어갈 때 예외 처리
     */
    public StoreImageLimitExceedException() {
        super("스토어에 이미지는 세장을 초과할 수 없습니다!!");
    }
}
