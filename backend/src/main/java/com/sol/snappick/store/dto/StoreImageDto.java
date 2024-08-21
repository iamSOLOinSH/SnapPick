package com.sol.snappick.store.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StoreImageDto {

    private String originImageUrl;

    private String thumbnailImageUrl;
}
