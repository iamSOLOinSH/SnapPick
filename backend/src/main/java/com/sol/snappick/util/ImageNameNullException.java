package com.sol.snappick.util;

public class ImageNameNullException extends RuntimeException {
    public ImageNameNullException() {
        super("이미지 이름이 비어있습니다.");
    }
}
