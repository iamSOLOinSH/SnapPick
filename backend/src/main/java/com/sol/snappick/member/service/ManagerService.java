package com.sol.snappick.member.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.sol.snappick.member.dto.AccountTransferReq;
import com.sol.snappick.member.dto.TransactionHistoryRes;
import com.sol.snappick.member.repository.MemberRepository;
import com.sol.snappick.util.fin.FinOpenApiHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class ManagerService {

    private final MemberRepository memberRepository;
    private final TransactionService transactionService;
    private final FinOpenApiHandler finOpenApiHandler;
    private final BasicMemberService basicMemberService;
    @Value("${finopenapi.userkey}")
    private String managerUserkey;

    // 계좌 현금 입금
    public TransactionHistoryRes deposit(AccountTransferReq accountTransferReq) {

        TransactionHistoryRes accountHolderRes = new TransactionHistoryRes();
        String accountNumber = accountTransferReq.getAccountNumber();

        ////// 1. 예금주 조회
        Map<String, Object> requestBody1 = new HashMap<>();
        requestBody1.put("accountNo", accountNumber);
        JsonNode jsonNode1 = finOpenApiHandler.apiRequest("/edu/demandDeposit/inquireDemandDepositAccountHolderName", "inquireDemandDepositAccountHolderName", HttpMethod.POST, requestBody1, managerUserkey);
        JsonNode responseData1 = jsonNode1.get("REC");

        accountHolderRes.setAccountNumber(accountNumber);
        accountHolderRes.setBankName(responseData1.get("bankName").asText());
        accountHolderRes.setName(responseData1.get("userName").asText());

        ////// 2. 계좌 입금


        Map<String, Object> requestBody2 = new HashMap<>();
        requestBody2.put("accountNo", accountNumber);
        requestBody2.put("transactionBalance", accountTransferReq.getBalance());
        requestBody2.put("transactionSummary", "(수시입출금) : 현금 입금");
        JsonNode jsonNode2 = finOpenApiHandler.apiRequest("/edu/demandDeposit/updateDemandDepositAccountDeposit", "updateDemandDepositAccountDeposit", HttpMethod.POST, requestBody2, managerUserkey);
        JsonNode responseData2 = jsonNode2.get("REC");

        String transactionUniqueNo = responseData2.get("transactionUniqueNo").asText();

        ////// 3. 거래내역 단건 조회
        String ownerUserkey = basicMemberService.getMemberById(accountHolderRes.getName()).getUserKey();

        Map<String, Object> requestBody3 = new HashMap<>();
        requestBody3.put("accountNo", accountNumber);
        requestBody3.put("transactionUniqueNo", transactionUniqueNo);
        JsonNode jsonNode3 = finOpenApiHandler.apiRequest("/edu/demandDeposit/inquireTransactionHistory", "inquireTransactionHistory", HttpMethod.POST, requestBody3, ownerUserkey);
        JsonNode responseData3 = jsonNode3.get("REC");
        finOpenApiHandler.printJson(responseData3);

        accountHolderRes.setBalance(responseData3.get("transactionBalance").asText());
        accountHolderRes.setAfterBalance(responseData3.get("transactionAfterBalance").asText());
        accountHolderRes.setTime(responseData3.get("transactionDate").asText() + responseData3.get("transactionTime").asText());

        return accountHolderRes;
    }

}
