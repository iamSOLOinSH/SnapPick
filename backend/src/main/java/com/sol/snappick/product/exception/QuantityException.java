package com.sol.snappick.product.exception;

public class QuantityException extends RuntimeException{
    public QuantityException() {
        super("재고 수량이 주문 수량보다 적습니다.");
    }

    public QuantityException(String message) {
        super(message);
    }
}
