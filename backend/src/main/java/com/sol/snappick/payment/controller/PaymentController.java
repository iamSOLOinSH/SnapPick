package com.sol.snappick.payment.controller;

import com.sol.snappick.payment.dto.ReceiptRes;
import com.sol.snappick.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("payment")
public class PaymentController {

    private final PaymentService paymentService;

    //    @PostMapping("/validate-pin")
    //    public ResponseEntity<Boolean> validatePin(
    //            Authentication authentication
    //            @RequestBody String pinCode
    //    ){
    //        memberId = Integer.valueOf(authentication.getName());
    //
    //        Boolean response = paymentService.validatePin(memberId, pinCode);
    //        return ResponseEntity.ok(response);
    //    }

    @GetMapping("/status")
    @Operation(summary = "수령 대기 고객 조회", description = "해당 store의 수령 대기 고객 정보를 조회할 수 있습니다.")
    public ResponseEntity<List<ReceiptRes>> getPendingCustomers(
        Authentication authentication,
        @RequestParam("store_id") Integer storeId
    ) {
        //TODO 권한 확인

        List<ReceiptRes> response = paymentService.getPendingCustomers(storeId);
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
        ReceiptRes response = paymentService.getReceipt(cartId, memberId);
        return ResponseEntity.ok()
                             .body(response);
    }
}
