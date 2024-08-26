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
public class StoreAPIDetailDto {
    private int storeDetailId;
    private int storeId;
    private String contents;
    private String images;
    private String relationStoreIds;
    private String notice;
    private String brandUrl;
    private String instaUrl;
//    private Boolean parking;
//    private boolean free;
//    private Boolean kids;
//    private boolean noKids;
//    private boolean food;
//    private boolean pet;
//    private boolean adult;
//    private boolean wifi;
//    private String etc;
//    private String etc2;
}