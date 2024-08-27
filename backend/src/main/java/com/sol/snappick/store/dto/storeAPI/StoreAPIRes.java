package com.sol.snappick.store.dto.storeAPI;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreAPIRes {
    private Boolean isSuccess;
    private Integer code;
    private List<StoreAPIDataDto> data;
}
