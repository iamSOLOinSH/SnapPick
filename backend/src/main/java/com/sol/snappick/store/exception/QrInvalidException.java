package com.sol.snappick.store.exception;

public class QrInvalidException extends RuntimeException {

    public QrInvalidException() {
        super("유효하지 않거나 만료된 QR 코드입니다.");
    }

    public QrInvalidException(String message) {
        super(message);
    }
}
