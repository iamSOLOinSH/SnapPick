package com.sol.snappick.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartPurchasedDto {

    private Integer cartId;
    private Integer transactionId;
    private Long purchasedAmount;
}
