package com.sol.snappick.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VisitedStoreDetailDto {

    private Integer storeId;
    private String name;
    private String location;

}
