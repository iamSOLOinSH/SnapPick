package com.sol.snappick.member.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.sol.snappick.util.fin.FinOpenApiHandler;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {

    private final FinOpenApiHandler finOpenApiHandler;
    private final BasicMemberService basicMemberService;

    @Value("${finopenapi.snappickAccount}")
    private String accountTypeUniqueNo;

    // 회원 등록
    @SneakyThrows
    public String postMember(String email) {

        // 1. 요청 본문 생성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("userId", email);

        // 2. api 요청
        JsonNode jsonNode = finOpenApiHandler.apiRequest("/member", HttpMethod.POST, requestBody);

        finOpenApiHandler.printJson(jsonNode);

        return finOpenApiHandler.getValueByKey(jsonNode, "userKey");
    }

    // 계좌 생성
    public String createAccount(String userKey) {
        // 1. 요청 본문 생성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("accountTypeUniqueNo", accountTypeUniqueNo);
        // 2. api 요청
        JsonNode jsonNode = finOpenApiHandler.apiRequest("/edu/demandDeposit/createDemandDepositAccount", "createDemandDepositAccount", HttpMethod.POST, requestBody, userKey);
        finOpenApiHandler.printJson(jsonNode);
        // 3. 응답값 받아서 반환
        String newAccountNo = jsonNode.get("REC").get("accountNo").textValue();
        return newAccountNo;
    }


}
