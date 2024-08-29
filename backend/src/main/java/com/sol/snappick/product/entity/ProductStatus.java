package com.sol.snappick.product.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "상품 상태")
public enum ProductStatus {
    판매가능, 품절
}
