package com.sol.snappick.product.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Schema(description = "상품 생성 요청 DTO")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProductCreateReq {

    private String name;
    private String description;
    private Integer price;
    private Integer totalStock;
    private Integer dailyLimit;
    private Integer personalLimit;

    @ArraySchema(schema = @Schema(implementation = ProductOptionReq.class))
    private List<ProductOptionReq> options;
}
