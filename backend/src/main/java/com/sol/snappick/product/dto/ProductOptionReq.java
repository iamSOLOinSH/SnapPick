package com.sol.snappick.product.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class ProductOptionReq {

    private String name;
    private Integer plusPrice;
    private Integer stock;

}