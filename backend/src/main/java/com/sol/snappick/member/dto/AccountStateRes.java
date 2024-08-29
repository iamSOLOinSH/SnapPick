package com.sol.snappick.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AccountStateRes {

    private String bankName;
    private String accountNumber;
    private Long theBalance;

}
