package com.sol.snappick.product.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카트 상태")
public enum CartStatus {
    결제대기, 결제완료, 수령완료
}