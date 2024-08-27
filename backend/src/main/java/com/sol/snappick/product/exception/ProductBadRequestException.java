package com.sol.snappick.product.exception;

public class ProductBadRequestException extends RuntimeException {

    public ProductBadRequestException() {
        super("상품에 대한 잘못된 요청입니다!");
    }

    public ProductBadRequestException(String message) {
        super(message);
    }
}
