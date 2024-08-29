package com.sol.snappick.cart.exception;

public class CartUnauthorizedException extends RuntimeException {

    public CartUnauthorizedException() {
        super("해당 카트에 접근 권한이 없습니다.");
    }
}
