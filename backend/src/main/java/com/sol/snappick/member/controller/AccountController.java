package com.sol.snappick.member.controller;

import com.sol.snappick.member.dto.AccountSingleReq;
import com.sol.snappick.member.dto.AccountTransferReq;
import com.sol.snappick.member.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static com.sol.snappick.global.ApiDescriptions.AccountController.*;
import static com.sol.snappick.global.ApiDescriptions.Headers.des_header_token;
import static com.sol.snappick.global.ApiDescriptions.common.des_input;
import static com.sol.snappick.global.ApiDescriptions.common.des_output;

@RestController
@RequestMapping("/accounts")
@Tag(name = "accounts", description = "멤버 : 계좌 관련 API")
@RequiredArgsConstructor
public class AccountController {

    private final TransactionService transactionService;

    @GetMapping
    @Operation(summary = "주계좌 번호와 금액 확인",
            description = des_header_token + des_output + des_AccountStateRes)
    public ResponseEntity<Void> getAccount(
            Authentication authentication
    ) {
        Integer memberId = Integer.valueOf(authentication.getName());
        // TODO
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/list")
    @Operation(summary = "타행 계좌 목록 조회(주계좌 제외)",
            description = des_header_token + des_output + des_AccountStateRes)
    public ResponseEntity<Void> getAccounts(
            Authentication authentication
    ) {
        Integer memberId = Integer.valueOf(authentication.getName());
        // TODO
        return ResponseEntity.ok().body(null);
    }


    @PostMapping
    @Operation(summary = "주계좌 지정(구매자용)",
            description = des_header_token + des_input + des_AccountSingleReq + des_output)
    public ResponseEntity<Void> updateAccount(
            Authentication authentication,
            @RequestBody AccountSingleReq accountSingleReq
    ) {
        Integer memberId = Integer.valueOf(authentication.getName());
        // TODO
        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/transfer")
    @Operation(summary = "타행 이체(판매자용)",
            description = des_header_token + des_input + des_AccountTransferReq + des_output)
    public ResponseEntity<Void> transfer(
            Authentication authentication,
            @RequestBody AccountTransferReq accountTransferReq
    ) {
        Integer memberId = Integer.valueOf(authentication.getName());
        // TODO
        return ResponseEntity.ok().body(null);
    }


    @PostMapping("/identity")
    @Operation(summary = "1원 송금 보내기",
            description = "타행으로 1원 송금을 하여 본인 인증을 시도합니다")
    public ResponseEntity<Void> beforeIdentification(
            Authentication authentication,
            AccountSingleReq accountSingleReq) {
        Integer memberId = Integer.valueOf(authentication.getName());
        // TODO
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/identity")
    @Operation(summary = "1원 송금 확인하기",
            description = "1원 송금 내역을 확인하고 본인 인증을 완료합니다")
    public ResponseEntity<Void> AfterIdentification(
            Authentication authentication,
            @RequestParam(name = "auth_code") String authCode
    ) {
        Integer memberId = Integer.valueOf(authentication.getName());
        // TODO
        return ResponseEntity.ok().body(null);
    }

}
