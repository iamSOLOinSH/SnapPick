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
public class ProductCreateReq {

    @Schema(description = "상품 이름")
    private String name;

    @Schema(description = "상품 설명")
    private String description;

    @Schema(description = "상품 가격")
    private Integer price;

    @Schema(description = "상품 재고")
    private Integer stock;

    @Schema(description = "상품 일일 물량 제한")
    private Integer dailyLimit;

    @Schema(description = "상품 1인 구매 제한")
    private Integer personalLimit;

}
