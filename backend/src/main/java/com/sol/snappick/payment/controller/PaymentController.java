package com.sol.snappick.payment.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sol.snappick.payment.dto.ReceiptRes;
import com.sol.snappick.payment.service.PaymentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "payment", description = "결제 : 결제 및 거래 관련 API")
@RequestMapping("payment")
public class PaymentController {

	private final PaymentService paymentService;

	@PostMapping()
	@Operation(summary = "결제 시도", description = "해당하는 cart를 결제할 수 있습니다.")
	public ResponseEntity<Boolean> attemptTransfer(
		Authentication authentication,
		@RequestParam("cartId") Integer cartId
	) {
		Integer memberId = Integer.valueOf(authentication.getName());
		Boolean response = paymentService.attemptTransfer(memberId,
														  cartId);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/status")
	@Operation(summary = "상품 전달 완료 처리", description = "수령완료로 카트의 상태를 갱신합니다.")
	public ResponseEntity<Boolean> cartItemReceived(
		Authentication authentication,
		@RequestBody Integer cartId
	) {
		Integer memberId = Integer.valueOf(authentication.getName());
		Boolean response = paymentService.cartItemReceived(cartId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/status")
	@Operation(summary = "수령 대기 고객 조회", description = "해당 store의 수령 대기 고객 정보를 조회할 수 있습니다.")
	public ResponseEntity<List<ReceiptRes>> getPendingCustomers(
		Authentication authentication,
		@RequestParam("store_id") Integer storeId
	) {
		Integer memberId = Integer.valueOf(authentication.getName());
		List<ReceiptRes> response = paymentService.getPendingCustomers(memberId,
																	   storeId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{cart_id}")
	@Operation(summary = "영수증 단일 조회", description = "영수증 단일 조회 API.")
	public ResponseEntity<ReceiptRes> getReceipt(
		@PathVariable("cart_id") Integer cartId,
		Authentication authentication
	) {
		// 현재 사용자 ID 식별
		Integer memberId = Integer.valueOf(authentication.getName());
		ReceiptRes response = paymentService.getReceipt(cartId,
														memberId);
		return ResponseEntity.ok()
							 .body(response);
	}
}
