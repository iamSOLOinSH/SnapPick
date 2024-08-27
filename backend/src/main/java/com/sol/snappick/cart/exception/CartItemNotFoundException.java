package com.sol.snappick.cart.exception;

public class CartItemNotFoundException extends RuntimeException{
    public CartItemNotFoundException() {
        super("카트에 담긴 상품을 찾을 수 없습니다.");
    }

    public CartItemNotFoundException(String message) {
        super(message);
    }
}

