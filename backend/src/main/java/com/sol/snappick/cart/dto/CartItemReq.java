package com.sol.snappick.cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "카트 상품 추가 요청 DTO")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemReq {

//    @Schema(description = "카트 id")
//    private Integer cartId;
    @Schema(description = "옵션 id")
    private Integer productOptionId;
    @Schema(description = "구매 수량")
    private Integer quantity;

}
