package com.sol.snappick.product.dto;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ProductOptionReq {

    private String name;
    private Integer plusPrice;
    private Integer stock;

}