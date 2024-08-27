package com.sol.snappick.product.exception;

public class ProductImageLimitExceedException extends RuntimeException {
    public ProductImageLimitExceedException(){
        super("상품에 이미지는 10장을 초과할 수 없습니다!");
    }
}
