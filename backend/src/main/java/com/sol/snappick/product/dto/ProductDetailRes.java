package com.sol.snappick.product.dto;

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
    private Integer totalStock;
    private Integer dailyLimit;
    private Integer personalLimit;

    @ArraySchema(schema = @Schema(implementation = ProductOptionRes.class))
    private List<ProductOptionRes> options;

    private List<String> originImageUrls;
}
