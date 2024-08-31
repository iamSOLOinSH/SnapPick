package com.sol.snappick.member.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.sol.snappick.member.dto.*;
import com.sol.snappick.member.entity.Member;
import com.sol.snappick.member.entity.Role;
import com.sol.snappick.member.entity.Transaction;
import com.sol.snappick.member.entity.TransactionType;
import com.sol.snappick.member.exception.BasicBadRequestException;
import com.sol.snappick.member.repository.MemberRepository;
import com.sol.snappick.member.repository.TransactionRepository;
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

        System.out.println(member.getIsOpenBank());
        System.out.println(member.getIsOpenBank() == false);
        // TODO
        // 1원 송금으로 오픈뱅킹 서비스 가입안한 사용자는 다른계좌 못봄
        if (member.getIsOpenBank() == false) {
            throw new BasicBadRequestException("1원 송금을 통해 본인인증을 완료해야합니다");
        }
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
        //
        if (member.getIsOpenBank() == false) {
            throw new BasicBadRequestException("잘못된 요청입니다. 1원 송금을 통해 본인인증을 완료해야합니다");
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


    // 1원 송금 확인
    public void checkAuth(Integer memberId, IdentificationReq identificationReq) {

        Member member = basicMemberService.getMemberById(memberId);

        // 1. 요청 본문 생성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("accountNo", identificationReq.getAccountNumber());
        requestBody.put("authText", "snappick");
        requestBody.put("authCode", identificationReq.getAuthCode());

        // 2. api 요청
        JsonNode jsonNode = finOpenApiHandler.apiRequest("/edu/accountAuth/checkAuthCode", "checkAuthCode", HttpMethod.POST, requestBody, member.getUserKey());

        // 3. 응답값 받아서 반환
        JsonNode responseData = jsonNode.get("REC");
        if (!responseData.get("status").asText().equals("SUCCESS")) {
            throw new BasicBadRequestException("Something went wrong");
        }

        // 4. 엔티티값 갱신
        member.setIsOpenBank(true);
        memberRepository.save(member);
    }


    // 돈 보내기
    public void sendMoney(Integer memberId, AccountTransferReq accountTransferReq) {

        Member member = basicMemberService.getMemberById(memberId);
        if (member.getRole() != Role.판매자) {
            throw new BasicBadRequestException("판매자만 돈을 보낼 수 있습니다");
        }
        if (member.getIsOpenBank() == false) {
            throw new BasicBadRequestException("잘못된 요청입니다. 1원 송금을 통해 본인인증을 완료해야합니다");
        }

        String userKey = member.getUserKey();
        String toAccountNo = accountTransferReq.getAccountNumber();
        String fromAccountNo = member.getAccountNumber();
        Long balance = accountTransferReq.getBalance();

        // 1. 예금주 조회
        Map<String, Object> requestBody1 = new HashMap<>();
        requestBody1.put("accountNo", toAccountNo);
        JsonNode jsonNode1 = finOpenApiHandler.apiRequest("/edu/demandDeposit/inquireDemandDepositAccountHolderName", "inquireDemandDepositAccountHolderName", HttpMethod.POST, requestBody1, userKey);
        JsonNode responseData1 = jsonNode1.get("REC");


        Integer userId = responseData1.get("userName").asInt();
        if (!memberId.equals(userId)) {
            throw new BasicBadRequestException("내 명의의 계좌가 아닙니다");
        }

        // 2. 이체
        String depositTransactionSummary = "snappick 입금";
        String withdrawalTransactionSummary = "송금 (" + responseData1.get("bankName").asText() + ")";


        Map<String, Object> requestBody2 = new HashMap<>();
        requestBody2.put("withdrawalAccountNo", fromAccountNo);
        requestBody2.put("depositAccountNo", toAccountNo);
        requestBody2.put("transactionBalance", balance);
        requestBody2.put("depositTransactionSummary", depositTransactionSummary);
        requestBody2.put("withdrawalTransactionSummary", withdrawalTransactionSummary);

        JsonNode jsonNode = finOpenApiHandler.apiRequest("/edu/demandDeposit/updateDemandDepositAccountTransfer", "updateDemandDepositAccountTransfer", HttpMethod.POST, requestBody2, userKey);

        JsonNode responseData2 = jsonNode.get("REC");
        if (!responseData2.isArray() || responseData2.size() == 0) {
            throw new BasicBadRequestException("Something went wrong");
        }

        // 3. 출금내역 transaction에 저장
        Integer transactionId = 0;
        for (JsonNode account : responseData2) {
            Integer transactionType = account.get("transactionType").asInt();
            if (transactionType == 2) {
                Transaction transaction = Transaction.builder()
                        .transactionUniqueNo(account.get("transactionUniqueNo").textValue())
                        .member(member)
                        .type(TransactionType.송금)
                        .fromAccountNo(fromAccountNo)
                        .toAccountNo(toAccountNo)
                        .variation(balance)
                        .summary(withdrawalTransactionSummary)
                        .transactedAt(LocalDateTime.now()).build();
                transactionRepository.save(transaction);
            }
        }
    }


    /////////////////////////////////////////////////////

    // 판매자의 거래내역 조회
    public List<TransactionDetailRes> checkTransactions(Integer memberId, String date) {
        Member member = basicMemberService.getMemberById(memberId);
        List<TransactionDetailRes> transactionList = new ArrayList<>();

        transactionRepository.findTransactionsByDateString(date).stream()
                .forEach(t -> {
                            if (t.getType() != TransactionType.출금 && t.getMember().equals(member)) {
                                // 추가검증하기
                                transactionList.add(TransactionDetailRes.fromEntity(t));
                            }
                        }
                );
        return transactionList;
    }

    // 결제하기
    public Integer transfer(Member buyer, // 구매자
                            Member seller, // 판매자
                            String withdrawalAccountNo, // 출금계좌(구매자꺼)
                            Long balance //거래금액
    ) {

        String phoneNumber = buyer.getPhoneNumber();
        String depositTransactionSummary = buyer.getName() + " (" + phoneNumber.substring(phoneNumber.lastIndexOf("-") + 1) + ")";
        String withdrawalTransactionSummary = "snappick 결제";

        // 1. 요청 본문 생성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("withdrawalAccountNo", withdrawalAccountNo);
        requestBody.put("depositAccountNo", seller.getAccountNumber());
        requestBody.put("transactionBalance", balance);
        requestBody.put("depositTransactionSummary", depositTransactionSummary);
        requestBody.put("withdrawalTransactionSummary", withdrawalTransactionSummary);

        // 2. api 요청
        JsonNode jsonNode = finOpenApiHandler.apiRequest("/edu/demandDeposit/updateDemandDepositAccountTransfer", "updateDemandDepositAccountTransfer", HttpMethod.POST, requestBody, buyer.getUserKey());

        // 3. 응답값 받아서 반환
        JsonNode responseData = jsonNode.get("REC");
        if (!responseData.isArray() || responseData.size() == 0) {
            throw new BasicBadRequestException("Something went wrong");
        }

        Integer transactionId = 0;
        // 4. 입출금 내역 transaction에 넣기
        for (JsonNode account : responseData) {
            Transaction transaction = Transaction.builder()
                    .transactionUniqueNo(account.get("transactionUniqueNo").textValue())
                    .fromAccountNo(account.get("accountNo").textValue())
                    .toAccountNo(account.get("transactionAccountNo").textValue())
                    .variation(balance)
                    .transactedAt(LocalDateTime.now()).build();

            Integer transactionType = account.get("transactionType").asInt();

            // 구매자
            if (transactionType == 2) {
                transaction.setMember(buyer);
                transaction.setType(TransactionType.출금);
                transaction.setSummary(withdrawalTransactionSummary);
                transactionId = transactionRepository.save(transaction).getId();
            } else {
                transaction.setMember(seller);
                transaction.setType(TransactionType.입금);
                transaction.setSummary(depositTransactionSummary);
                transactionRepository.save(transaction);
            }

        }

        if (transactionId == 0) {
            throw new BasicBadRequestException("Something went wrong");
        }
        return transactionId;

    }
}
