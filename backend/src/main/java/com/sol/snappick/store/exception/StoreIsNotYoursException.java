package com.sol.snappick.store.exception;

public class StoreIsNotYoursException extends RuntimeException {
	public StoreIsNotYoursException() {
		super("해당 팝업 스토어의 소유주가 아닙니다.");
	}
}
