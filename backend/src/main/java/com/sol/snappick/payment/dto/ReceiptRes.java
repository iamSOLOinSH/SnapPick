package com.sol.snappick.payment.dto;

import com.sol.snappick.cart.dto.CartItemRes;
import com.sol.snappick.product.entity.CartStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ReceiptRes {

    @Schema(description = "카트 Id")
    private Integer id;
    @Schema(description = "처리 상테")
    private CartStatus status;
    @Schema(description = "스토어")
    private StoreSimpleRes store;
    @Schema(description = "구매자")
    private CustomerRes customer;
    @Schema(description = "결제 총액")
    private Long totalPrice;
    @Schema(description = "결제 시간")
    private LocalDateTime transactedAt;
    @Schema(description = "카트 상품 목록")
    private List<CartItemRes> items;

}
