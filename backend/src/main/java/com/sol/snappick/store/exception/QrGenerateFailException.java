package com.sol.snappick.store.exception;

public class QrGenerateFailException extends RuntimeException {

    public QrGenerateFailException() {
        super("QR을 생성하는데 실패했습니다.");
    }
}
