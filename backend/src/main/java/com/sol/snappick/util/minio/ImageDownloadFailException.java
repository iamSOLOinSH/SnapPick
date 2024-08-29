package com.sol.snappick.util.minio;

public class ImageDownloadFailException extends RuntimeException {
	public ImageDownloadFailException () {
		super("이미지를 내려받는데 실패했습니다.");
	}
}
