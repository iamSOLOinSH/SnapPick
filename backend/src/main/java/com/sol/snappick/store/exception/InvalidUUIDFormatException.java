package com.sol.snappick.store.exception;

public class InvalidUUIDFormatException extends RuntimeException {
	public InvalidUUIDFormatException() {
		super("잘못된 UUID 형식입니다.");
	}
}
