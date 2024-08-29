package com.sol.snappick.product.dto;

import com.sol.snappick.product.entity.ProductStatus;
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
    private Long price;
    private Integer stock;
    private ProductStatus status;
    private List<String> thumbnailImageUrls;

}
