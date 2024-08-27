package com.sol.snappick.cart.dto;

import com.sol.snappick.product.entity.Cart;
import com.sol.snappick.product.entity.ProductOption;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CartItemRes {

    @Schema(description = "카트 상품 id")
    private Integer id;
    @Schema(description = "카트 id")
    private Integer cartId;
    @Schema(description = "옵션")
    private ProductOption productOption;
    @Schema(description = "구매 수량")
    private Integer quantity;

}
