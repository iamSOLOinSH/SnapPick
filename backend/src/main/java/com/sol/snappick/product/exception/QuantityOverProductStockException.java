package com.sol.snappick.product.exception;

public class QuantityOverProductStockException extends RuntimeException{
    public QuantityOverProductStockException() {
        super("재고 수량이 주문 수량보다 적습니다.");
    }

    public QuantityOverProductStockException(String message) {
        super(message);
    }
}
