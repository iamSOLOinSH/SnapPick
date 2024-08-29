package com.sol.snappick.payment.controller;

import com.sol.snappick.payment.dto.ReceiptRes;
import com.sol.snappick.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import okhttp3.Response;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping()
    public ResponseEntity<Boolean> attemptPayment(
            @RequestBody Integer cartId
    ){
        //memberId

        Boolean response = paymentService.attemptPayment(cartId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status")
    @Operation(summary = "수령 대기 고객 조회", description = "해당 store의 수령 대기 고객 정보를 조회할 수 있습니다.")
    public ResponseEntity<List<ReceiptRes>> getPendingCustomers(
            Authentication authentication,
            @RequestParam("store_id") Integer storeId
    ){
        //TODO 권한 확인

        List<ReceiptRes> response = paymentService.getPendingCustomers(storeId);
        return ResponseEntity.ok(response);
    }

}
