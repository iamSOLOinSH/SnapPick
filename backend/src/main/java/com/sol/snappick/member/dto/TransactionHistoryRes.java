package com.sol.snappick.member.dto;

import com.sol.snappick.global.CommonFormatter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionHistoryRes {

    private String name;
    private String bankName;
    private String accountNumber;
    private String balance;
    private String afterBalance;
    private String time;


    public void setTime(String time) {
        this.time = CommonFormatter.timeFormat(time);
    }

    public void setBalance(String balance) {
        this.balance = CommonFormatter.numberFormat(balance);
    }

    public void setAfterBalance(String afterBalance) {
        this.afterBalance = CommonFormatter.numberFormat(afterBalance);
    }
}
