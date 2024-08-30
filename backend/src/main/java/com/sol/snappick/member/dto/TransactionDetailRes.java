package com.sol.snappick.member.dto;

import com.sol.snappick.global.CommonFormatter;
import com.sol.snappick.member.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TransactionDetailRes {

    private String transactionType;
    private String balance;
    private String afterBalance;
    private String summary;
    private String time;

    public static TransactionDetailRes fromEntity(Transaction transaction) {
        return TransactionDetailRes.builder()
                .transactionType(transaction.getType().name())
                .balance(String.format("%,d", transaction.getVariation()) + "원")
                .summary(transaction.getSummary())
                .time(CommonFormatter.timeFormat(transaction.getTransactedAt())).build();

    }
}
