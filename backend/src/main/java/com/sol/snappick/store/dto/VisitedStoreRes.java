package com.sol.snappick.store.dto;

import com.sol.snappick.product.dto.CartPurchasedDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VisitedStoreRes {

    private VisitedStoreDetailDto storeDetailDto;
    private StoreVisitDto storeVisitDto;
    private CartPurchasedDto cartPurchasedDto;
}
