package com.sol.snappick.member.controller;

import com.sol.snappick.member.dto.AccountTransferReq;
import com.sol.snappick.member.dto.TransactionDetailRes;
import com.sol.snappick.member.dto.TransactionHistoryRes;
import com.sol.snappick.member.service.ManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/manager")
@Tag(name = "manager", description = "멤버 : 관리자 API")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    @GetMapping("/")
    @Operation(summary = "개발중 == 거래조회(대사)")
    public ResponseEntity<Void> checkTransactions(
    ) {
        // TODO
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/history")
    @Operation(summary = "계좌 거래내역 조회")
    public ResponseEntity<ArrayList<TransactionDetailRes>> getTransaction(
            @RequestParam(name = "account_no") String accouuntNo
    ) {
        return ResponseEntity.ok().body(
                managerService.getTransaction(accouuntNo));
    }

    @PostMapping("/deposit")
    @Operation(summary = "현금 입금")
    public ResponseEntity<TransactionHistoryRes> deposit(
            @RequestBody AccountTransferReq accountTransferReq
    ) {
        // TODO security
        return ResponseEntity.ok().body(
                managerService.deposit(accountTransferReq));
    }
}
