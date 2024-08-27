package com.sol.snappick.member.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "거래 유형")
public enum TransactionType {
    입금, //0
    송금, //1
    환불 //2
}
