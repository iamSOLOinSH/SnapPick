package com.sol.snappick.util;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ImageUploadRes {

    private String originImageUrl;
    private String thumbnailImageUrl;
}
