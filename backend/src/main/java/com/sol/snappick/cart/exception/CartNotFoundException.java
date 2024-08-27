package com.sol.snappick.cart.exception;

public class CartNotFoundException extends RuntimeException{
    public CartNotFoundException() {
        super("카트를 찾을 수 없습니다.");
    }

    public CartNotFoundException(String message) {
        super(message);
    }
}
