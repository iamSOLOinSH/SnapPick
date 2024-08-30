package com.sol.snappick.member.dto;

import com.sol.snappick.product.dto.CartPurchasedDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TodayTransactionRes {

    private Long totalAmount;
    private Long cnt;

    private List<TransactionDetailRes> wrongTransactionList = new ArrayList<>();
    private List<CartPurchasedDto> wrongCartList = new ArrayList<>();

}
