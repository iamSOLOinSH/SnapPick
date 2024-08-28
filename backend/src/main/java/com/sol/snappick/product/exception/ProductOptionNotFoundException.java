package com.sol.snappick.product.exception;

public class ProductOptionNotFoundException extends RuntimeException {

    public ProductOptionNotFoundException() {
        super("존재하지 않는 상품입니다.");
    }

    public ProductOptionNotFoundException(String message) {
        super(message);
    }
}

