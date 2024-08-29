package com.sol.snappick.store.controller;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sol.snappick.store.exception.QrInvalidException;
import com.sol.snappick.store.service.QrService;
import com.sol.snappick.store.service.StoreService;
import com.sol.snappick.store.service.StoreVisitService;
import com.sol.snappick.store.util.JwtUtil;
import com.sol.snappick.util.QrUtil;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store/qr")
public class QrController {

	private final JwtUtil jwtUtil;
	private final QrUtil qrUtil;
	private final QrService qrService;
	private final StoreVisitService storeVisitService;
	private final StoreService storeService;

	@PostMapping("/{store_uuid}")
	@Operation(summary = "QR 코드 생성", description = "store의 uuid를 넣어서 해당 스토어로 입장할 수 있는 QR을 생성하는 API")
	public ResponseEntity<byte[]> makeQr(
			@PathVariable("store_uuid") String uuid,
			// 기본 3분
			@RequestParam(defaultValue = "180000", required = false, value = "duration") long duration
	) {
		byte[] response = qrService.generateQrCode(uuid,
												   duration);
		return ResponseEntity.ok()
							 .body(response);
	}

	// QR 코드 검증 엔드포인트
	@GetMapping("/validate")
	@Operation(summary = "QR 코드 검증 및 방문 처리", description = "QR코드를 검증하고, 스토어 방문을 처리합니다.")
	public ResponseEntity<Void> validateQrAndVisitStore(
			@RequestParam("token") String token,
			Authentication authentication
	) {
		// 현재 사용자 ID 식별
		Integer memberId = Integer.valueOf(authentication.getName());

		// QR 코드 토큰 검증
		if(!jwtUtil.validateToken(token)) {
			throw new QrInvalidException();
		}

		// QR 코드로부터 스토어 ID 받아오기
		String storeUUID = jwtUtil.getStoreUUIDFromToken(token);

		Integer storeId = storeService.getStoreIdByUUID(storeUUID);

		// 스토어 방문 기록 추가 및 장바구니 기능 권한 부여
		storeVisitService.recordVisit(storeId,
									  memberId,
									  true);

		// TODO : 프론트랑 url 상의 필요
		// 스토어 상세 페이지로 리다이렉트
		String redirectUrl = "/store/view?storeId=" + storeId + "&qr=true";
		return ResponseEntity.status(HttpStatus.FOUND)
							 .location(URI.create(redirectUrl))
							 .build();

	}
}
