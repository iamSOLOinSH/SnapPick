package com.sol.snappick.store.dto.storeAPI;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreAPIImageDto {
    private int storeImageId;
    private int storeId;
    private String url;
    private String name;
    private String type;
    private long size;
    private String fileHash;
}