package com.sol.snappick.member.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "거래 유형")
public enum TransactionType {
    송금, //0 (판매자가 타행 계좌로 돈을 송금)
    입금, //1 (구매자가 스토어에서 결제함)
    출금 //2  (판매자가 상품을 판매함)
}
