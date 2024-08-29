package com.sol.snappick.store.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class StoreRes {

    private Integer id;

    private String name;

    private String description;

    private String location;

    private Double latitude;

    private Double longitude;

    private LocalDate operateStartAt;

    private LocalDate operateEndAt;

    private int viewCount;

    private int visitCount;

    private Integer sellerId;

    private List<String> tags;

    private List<StoreImageDto> images;

    private List<StoreRunningTimeDto> runningTimes;

}
