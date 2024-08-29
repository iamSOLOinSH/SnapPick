package com.sol.snappick.member.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.sol.snappick.member.dto.AccountSingleReq;
import com.sol.snappick.member.dto.AccountStateRes;
import com.sol.snappick.member.entity.Member;
import com.sol.snappick.member.entity.Role;
import com.sol.snappick.member.entity.Transaction;
import com.sol.snappick.member.entity.TransactionType;
import com.sol.snappick.member.exception.BasicBadRequestException;
import com.sol.snappick.member.repository.MemberRepository;
import com.sol.snappick.member.repository.TransactionRepository;
import com.sol.snappick.product.repository.CartRepository;
import com.sol.snappick.util.fin.FinOpenApiHandler;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private final CartRepository cartRepository;
    private final TransactionRepository transactionRepository;

    @Value("${finopenapi.snappickAccount}")
    private String accountTypeUniqueNo;

    // 회원 등록
    @SneakyThrows
    public String postMember(String email) {

        // 1. 요청 본문 생성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("userId", email);

        JsonNode jsonNode;
        // 2. api 요청
        try {
            jsonNode = finOpenApiHandler.apiRequest("/member", HttpMethod.POST, requestBody);
        } catch (Exception e) {
            jsonNode = finOpenApiHandler.apiRequest("/member/search", HttpMethod.POST, requestBody);
        }

        return finOpenApiHandler.getValueByKey(jsonNode, "userKey");
    }

    // 계좌 생성
    public String createAccount(String userKey) {
        // 1. 요청 본문 생성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("accountTypeUniqueNo", accountTypeUniqueNo);
        // 2. api 요청
        JsonNode jsonNode = finOpenApiHandler.apiRequest(
                "/edu/demandDeposit/createDemandDepositAccount", "createDemandDepositAccount",
                HttpMethod.POST, requestBody, userKey
        );
        finOpenApiHandler.printJson(jsonNode);
        // 3. 응답값 받아서 반환
        String newAccountNo = jsonNode.get("REC")
                .get("accountNo")
                .textValue();
        return newAccountNo;
    }

    // 주 계좌 확인하기
    @Transactional(readOnly = true)
    public AccountStateRes getMyAccount(Integer memberId) {
        Member member = basicMemberService.getMemberById(memberId);

        String myAccount = member.getAccountNumber();
        if (myAccount == null) {
            return null;
        }

        // 1. 요청 본문 생성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("accountNo", myAccount);

        // 2. api 요청
        JsonNode jsonNode = finOpenApiHandler.apiRequest(
                "/edu/demandDeposit/inquireDemandDepositAccount", "inquireDemandDepositAccount",
                HttpMethod.POST, requestBody, member.getUserKey()
        );

        // 3. 응답값 받아서 반환
        JsonNode responseData = jsonNode.get("REC");

        return AccountStateRes.builder()
                .bankName(responseData.get("bankName")
                        .textValue())
                .accountNumber(myAccount)
                .theBalance(Long.valueOf(responseData.get("accountBalance")
                        .textValue()))
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
        JsonNode jsonNode = finOpenApiHandler.apiRequest(
                "/edu/demandDeposit/inquireDemandDepositAccountList", "inquireDemandDepositAccountList",
                HttpMethod.POST, requestBody, member.getUserKey()
        );

        // 3. 응답값 받아서 반환
        JsonNode responseData = jsonNode.get("REC");

        List<AccountStateRes> accountList = new ArrayList<>();
        if (!responseData.isArray()) {
            return accountList;
        }

        for (JsonNode account : responseData) {
            if (account.get("accountNo")
                    .textValue()
                    .equals(myAccount)) {
                continue;
            }
            accountList.add(AccountStateRes.builder()
                    .bankName(account.get("bankName")
                            .textValue())
                    .accountNumber(account.get("accountNo")
                            .textValue())
                    .theBalance(Long.valueOf(account.get("accountBalance")
                            .textValue()))
                    .build());
        }

        return accountList;
    }

    // 주 계좌 등록
    public AccountStateRes setMyAccount(
            Integer memberId,
            AccountSingleReq accountSingleReq
    ) {
        Member member = basicMemberService.getMemberById(memberId);
        String accountNumber = accountSingleReq.getAccountNumber();
        if (member.getRole() == Role.판매자) {
            throw new BasicBadRequestException("판매자는 주계좌를 변경할 수 없습니다");
        }

        // 내 계좌목록 불러오기
        Map<String, Object> requestBody = new HashMap<>();
        JsonNode jsonNode = finOpenApiHandler.apiRequest(
                "/edu/demandDeposit/inquireDemandDepositAccountList", "inquireDemandDepositAccountList",
                HttpMethod.POST, requestBody, member.getUserKey()
        );
        JsonNode responseData = jsonNode.get("REC");

        for (JsonNode account : responseData) {
            if (account.get("accountNo")
                    .textValue()
                    .equals(accountNumber)) {
                member.changeAccountNumber(accountNumber);
                memberRepository.save(member);

                return AccountStateRes.builder()
                        .bankName(account.get("bankName")
                                .textValue())
                        .accountNumber(account.get("accountNo")
                                .textValue())
                        .theBalance(Long.valueOf(account.get("accountBalance")
                                .textValue()))
                        .build();
            }
        }

        throw new BasicBadRequestException("내 계좌 목록에서 해당 계좌를 찾을 수 없습니다");
    }


    // 1원 송금
    public String openAuth(Integer memberId, String accountNumber) {

        Member member = basicMemberService.getMemberById(memberId);

        // 1. 요청 본문 생성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("accountNo", accountNumber);
        requestBody.put("authText", "snappick");

        // 2. api 요청
        JsonNode jsonNode = finOpenApiHandler.apiRequest("/edu/accountAuth/openAccountAuth", "openAccountAuth", HttpMethod.POST, requestBody, member.getUserKey());

        // 3. 응답값 받아서 반환
        JsonNode responseData = jsonNode.get("REC");

        return responseData.get("transactionUniqueNo").textValue();

    }


    /////////////////////////////////////////////////////


    public Boolean transfer(Member member, // 구매자
                            String withdrawalAccountNo, // 출금계좌(구매자꺼)
                            String depositAccountNo, //입금계좌(스토어 주인의 accountNumber)
                            Integer balance //거래금액
    ) {

        String phoneNumber = member.getPhoneNumber();

        // 1. 요청 본문 생성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("withdrawalAccountNo", withdrawalAccountNo);
        requestBody.put("depositAccountNo", depositAccountNo);
        requestBody.put("transactionBalance", balance);
        requestBody.put("depositTransactionSummary", member.getName() + " (" + phoneNumber.substring(phoneNumber.lastIndexOf("-") + 1) + ")");
        requestBody.put("withdrawalTransactionSummary", "snappick 결제");

        // 2. api 요청
        JsonNode jsonNode = finOpenApiHandler.apiRequest("/edu/demandDeposit/updateDemandDepositAccountTransfer", "updateDemandDepositAccountTransfer", HttpMethod.POST, requestBody, member.getUserKey());

        // 3. 응답값 받아서 반환
        JsonNode responseData = jsonNode.get("REC");
        if (!responseData.isArray() || responseData.size() == 0) {
            return false;
        }

        // 4. 입출금 내역 transaction에 넣기
        for (JsonNode account : responseData) {
            transactionRepository.save(
                    Transaction.builder()
                            .member(member)
                            .transactionUniqueNo(account.get("transactionUniqueNo").textValue())
                            .accountNo(account.get("accountNo").textValue())
                            .transactionAccountNo(account.get("transactionAccountNo").textValue())
                            .variation(balance)
                            .type(TransactionType.values()[Integer.parseInt(account.get("transactionType").textValue())])
                            .transactedAt(LocalDateTime.now()).build()
            );
        }


        return true;

    }
}
