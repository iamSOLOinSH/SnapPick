package com.sol.snappick.member.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.sol.snappick.member.dto.AccountSingleReq;
import com.sol.snappick.member.dto.AccountStateRes;
import com.sol.snappick.member.entity.Member;
import com.sol.snappick.member.entity.Role;
import com.sol.snappick.member.exception.BasicBadRequestException;
import com.sol.snappick.member.repository.MemberRepository;
import com.sol.snappick.util.fin.FinOpenApiHandler;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {

    private final FinOpenApiHandler finOpenApiHandler;
    private final BasicMemberService basicMemberService;
    private final MemberRepository memberRepository;

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

    // 주 계좌 확인하기
    @Transactional(readOnly = true)
    public AccountStateRes getMyAccount(Integer memberId) {
        Member member = basicMemberService.getMemberById(memberId);

        String myAccount = member.getAccountNumber();
        if (myAccount == null) return null;

        // 1. 요청 본문 생성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("accountNo", myAccount);

        // 2. api 요청
        JsonNode jsonNode = finOpenApiHandler.apiRequest("/edu/demandDeposit/inquireDemandDepositAccount", "inquireDemandDepositAccount", HttpMethod.POST, requestBody, member.getUserKey());

        // 3. 응답값 받아서 반환
        JsonNode responseData = jsonNode.get("REC");

        return AccountStateRes.builder()
                .bankName(responseData.get("bankName").textValue())
                .accountNumber(myAccount)
                .theBalance(Long.valueOf(responseData.get("accountBalance").textValue()))
                .build();

    }

    // 타행 계좌 확인하기
    @Transactional(readOnly = true)
    public List<AccountStateRes> getOtherAccount(Integer memberId) {
        Member member = basicMemberService.getMemberById(memberId);

        String myAccount = member.getAccountNumber();

        // 1. 요청 본문 생성
        Map<String, Object> requestBody = new HashMap<>();

        // 2. api 요청
        JsonNode jsonNode = finOpenApiHandler.apiRequest("/edu/demandDeposit/inquireDemandDepositAccountList", "inquireDemandDepositAccountList", HttpMethod.POST, requestBody, member.getUserKey());

        // 3. 응답값 받아서 반환
        JsonNode responseData = jsonNode.get("REC");

        List<AccountStateRes> accountList = new ArrayList<>();
        if (!responseData.isArray()) {
            return accountList;
        }

        for (JsonNode account : responseData) {
            if (account.get("accountNo").textValue().equals(myAccount)) {
                continue;
            }
            accountList.add(
                    AccountStateRes.builder()
                            .bankName(account.get("bankName").textValue())
                            .accountNumber(account.get("accountNo").textValue())
                            .theBalance(Long.valueOf(account.get("accountBalance").textValue()))
                            .build()
            );
        }


        return accountList;
    }

    // 주 계좌 등록
    public AccountStateRes setMyAccount(Integer memberId, AccountSingleReq accountSingleReq) {
        Member member = basicMemberService.getMemberById(memberId);
        String accountNumber = accountSingleReq.getAccountNumber();
        if (member.getRole() == Role.판매자) {
            throw new BasicBadRequestException("판매자는 주계좌를 변경할 수 없습니다");
        }

        // 내 계좌목록 불러오기
        Map<String, Object> requestBody = new HashMap<>();
        JsonNode jsonNode = finOpenApiHandler.apiRequest("/edu/demandDeposit/inquireDemandDepositAccountList", "inquireDemandDepositAccountList", HttpMethod.POST, requestBody, member.getUserKey());
        JsonNode responseData = jsonNode.get("REC");

        for (JsonNode account : responseData) {
            if (account.get("accountNo").textValue().equals(accountNumber)) {
                member.changeAccountNumber(accountNumber);
                memberRepository.save(member);

                return AccountStateRes.builder()
                        .bankName(account.get("bankName").textValue())
                        .accountNumber(account.get("accountNo").textValue())
                        .theBalance(Long.valueOf(account.get("accountBalance").textValue()))
                        .build();
            }
        }

        throw new BasicBadRequestException("내 계좌 목록에서 해당 계좌를 찾을 수 없습니다");
    }
}
