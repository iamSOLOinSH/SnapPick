package com.sol.snappick.product.dto;

import lombok.*;

@ToString
@Getter
@NoArgsConstructor
public class ProductOptionReq {

    private String name;
    private Integer plusPrice;
    private Integer stock;

    @Builder
    public ProductOptionReq(String name, Integer plusPrice, Integer stock) {
        this.name = name;
        this.plusPrice = plusPrice;
        this.stock = stock;
    }

}