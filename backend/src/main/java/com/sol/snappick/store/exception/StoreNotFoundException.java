package com.sol.snappick.store.exception;

public class StoreNotFoundException extends RuntimeException {
	public StoreNotFoundException () {
		super("스토어를 찾을 수 없습니다.");
	}
}
