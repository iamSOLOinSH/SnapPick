package com.sol.snappick.product.dto;

import com.sol.snappick.product.entity.ProductStatus;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProductDetailRes {

    private Integer id;
    private String name;
    private String description;
    private Integer price;
    private Integer stock;
    private Integer dailyLimit;
    private Integer personalLimit;
    private ProductStatus status;
    private List<String> originImageUrls;
}
