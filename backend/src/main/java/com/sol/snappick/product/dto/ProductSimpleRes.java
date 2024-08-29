package com.sol.snappick.product.dto;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProductSimpleRes {

    private Integer id;
    private String name;
    private Integer price;
    private Integer stock;
    private List<String> thumbnailImageUrls;

}
