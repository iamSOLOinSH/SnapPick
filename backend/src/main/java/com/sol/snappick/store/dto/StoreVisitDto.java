package com.sol.snappick.store.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreVisitDto {

    private Integer storeVisitId;
    private Integer cartId;
    private LocalDateTime visitedAt;
}
