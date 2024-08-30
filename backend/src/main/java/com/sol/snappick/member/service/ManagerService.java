package com.sol.snappick.member.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.sol.snappick.global.CommonFormatter;
import com.sol.snappick.member.dto.AccountTransferReq;
import com.sol.snappick.member.dto.TodayTransactionRes;
import com.sol.snappick.member.dto.TransactionDetailRes;
import com.sol.snappick.member.dto.TransactionHistoryRes;
import com.sol.snappick.member.entity.TransactionType;
import com.sol.snappick.member.exception.BasicBadRequestException;
import com.sol.snappick.member.repository.TransactionRepository;
import com.sol.snappick.util.fin.FinOpenApiHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static com.sol.snappick.global.CommonFormatter.yyyyMMddFormat;

@Service
@Transactional
@RequiredArgsConstructor
public class ManagerService {

    private final FinOpenApiHandler finOpenApiHandler;
    private final BasicMemberService basicMemberService;
    private final TransactionRepository transactionRepository;

    @Value("${finopenapi.userkey}")
    private String managerUserkey;

    // 거래조회
    public TodayTransactionRes checkTransactions(String date) {

        if (date.length() != 8) {
            throw new BasicBadRequestException("입력값은 '20240901'와 같은 형식의 8글자여야 합니다");
        }


        TodayTransactionRes todayTransactionRes = new TodayTransactionRes();
        AtomicReference<Long> totalAmount = new AtomicReference<>(0L);
        AtomicReference<Long> cnt = new AtomicReference<>(0L);


        transactionRepository.findTransactionsByDateString(date).stream()
                .forEach(t -> {
                            if (t.getType() == TransactionType.출금) {
                                // 추가검증하기
                                cnt.updateAndGet(v -> v + 1);
                                totalAmount.updateAndGet(v -> v + t.getVariation());
                            }
                        }
                );
        todayTransactionRes.setTotalAmount(totalAmount.get());
        todayTransactionRes.setCnt(cnt.get());

        return todayTransactionRes;
    }

    // 예금주 조회
    private JsonNode inquireDemandDepositAccountHolderName(String accountNumber) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("accountNo", accountNumber);
        JsonNode jsonNode = finOpenApiHandler.apiRequest("/edu/demandDeposit/inquireDemandDepositAccountHolderName", "inquireDemandDepositAccountHolderName", HttpMethod.POST, requestBody, managerUserkey);
        return jsonNode.get("REC");
    }

    // 계좌 현금 입금
    public TransactionHistoryRes deposit(AccountTransferReq accountTransferReq) {

        TransactionHistoryRes accountHolderRes = new TransactionHistoryRes();
        String accountNumber = accountTransferReq.getAccountNumber();

        ////// 1. 예금주 조회
        JsonNode responseData1 = inquireDemandDepositAccountHolderName(accountNumber);

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

        accountHolderRes.setBalance(responseData3.get("transactionBalance").asText());
        accountHolderRes.setAfterBalance(responseData3.get("transactionAfterBalance").asText());
        accountHolderRes.setTime(responseData3.get("transactionDate").asText() + responseData3.get("transactionTime").asText());

        return accountHolderRes;
    }


    // 계좌 거래내역 조회
    public ArrayList<TransactionDetailRes> getTransaction(String accountNumber) {


        ////// 1. 예금주 조회
        JsonNode responseData1 = inquireDemandDepositAccountHolderName(accountNumber);
        Integer memberId = responseData1.get("userName").asInt();
        String userKey = basicMemberService.getMemberById(memberId).getUserKey();
        ////// 2. 거래내역 조회
        ZoneId SEOUL_ZONE = ZoneId.of("Asia/Seoul");

        LocalDate now = LocalDate.now(SEOUL_ZONE);
        String endDate = yyyyMMddFormat(now);
        String startDate = yyyyMMddFormat(now.minusDays(6));


        Map<String, Object> requestBody2 = new HashMap<>();
        requestBody2.put("accountNo", accountNumber);
        requestBody2.put("startDate", startDate);
        requestBody2.put("endDate", endDate);
        requestBody2.put("transactionType", "A");
        requestBody2.put("orderByType", "DESC");
        JsonNode jsonNode2 = finOpenApiHandler.apiRequest("/edu/demandDeposit/inquireTransactionHistoryList", "inquireTransactionHistoryList", HttpMethod.POST, requestBody2, userKey);
        JsonNode responseData2 = jsonNode2.get("REC");

        ArrayList<TransactionDetailRes> transactionList = new ArrayList<>();

        for (JsonNode t : responseData2.get("list")) {
            transactionList.add(
                    TransactionDetailRes
                            .builder()
                            .transactionType(t.get("transactionTypeName").asText())
                            .balance(CommonFormatter.numberFormat(t.get("transactionBalance").asText()))
                            .afterBalance(CommonFormatter.numberFormat(t.get("transactionAfterBalance").asText()))
                            .summary(t.get("transactionSummary").asText())
                            .time(CommonFormatter.timeFormat(t.get("transactionDate").asText() + t.get("transactionTime").asText()))
                            .build()
            );
        }
        return transactionList;
    }


}
