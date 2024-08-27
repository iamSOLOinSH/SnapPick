package com.sol.snappick.store.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StoreImageDto {

    private String originImageUrl;

    private String thumbnailImageUrl;

    @Builder
    public StoreImageDto(
        String originImageUrl,
        String thumbnailImageUrl
    ) {
        this.originImageUrl = originImageUrl;
        this.thumbnailImageUrl = thumbnailImageUrl;
    }
}
