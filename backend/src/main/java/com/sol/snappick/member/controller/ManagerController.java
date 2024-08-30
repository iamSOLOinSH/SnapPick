package com.sol.snappick.member.controller;

import com.sol.snappick.member.dto.AccountTransferReq;
import com.sol.snappick.member.dto.TodayTransactionRes;
import com.sol.snappick.member.dto.TransactionDetailRes;
import com.sol.snappick.member.dto.TransactionHistoryRes;
import com.sol.snappick.member.exception.BasicBadRequestException;
import com.sol.snappick.member.service.ManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

import static com.sol.snappick.global.ApiDescriptions.AccountController.des_string_date;
import static com.sol.snappick.global.ApiDescriptions.common.des_input;

@RestController
@RequestMapping("/manager")
@Tag(name = "manager", description = "멤버 : 관리자 API")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;
    @Value("${finopenapi.secretkey}")
    private String secretKey;

    @GetMapping("/transaction/inquiry")
    @Operation(summary = "거래조회(대사)", description = des_input + des_string_date)
    public ResponseEntity<TodayTransactionRes> checkTransactions(
            @RequestParam(name = "secret_key") String secretKey,
            @RequestParam(name = "date") String date
    ) {
        // TODO 데이터 쌓인 후 테스트
        checkKey(secretKey);
        return ResponseEntity.ok().body(managerService.checkTransactions(date));
    }

    @GetMapping("/history")
    @Operation(summary = "계좌 거래내역 조회")
    public ResponseEntity<ArrayList<TransactionDetailRes>> getTransaction(
            @RequestParam(name = "secret_key") String secretKey,
            @RequestParam(name = "account_no") String accouuntNo
    ) {
        checkKey(secretKey);
        return ResponseEntity.ok().body(
                managerService.getTransaction(accouuntNo));
    }

    @PostMapping("/deposit")
    @Operation(summary = "현금 입금")
    public ResponseEntity<TransactionHistoryRes> deposit(
            @RequestParam(name = "secret_key") String secretKey,
            @RequestBody AccountTransferReq accountTransferReq
    ) {
        checkKey(secretKey);
        return ResponseEntity.ok().body(
                managerService.deposit(accountTransferReq));
    }

    //////////////////////////////////

    private void checkKey(String secretKey) {
        if (!secretKey.equals(this.secretKey)) {
            throw new BasicBadRequestException("secret_key가 일치하지 않습니다!");
        }
    }
}
