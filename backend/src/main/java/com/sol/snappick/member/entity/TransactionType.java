package com.sol.snappick.member.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "거래 유형")
public enum TransactionType {
    환불, //0
    입금, //1
    출금 //2
}
