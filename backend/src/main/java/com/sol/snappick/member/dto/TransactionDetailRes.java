package com.sol.snappick.member.dto;

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

}
