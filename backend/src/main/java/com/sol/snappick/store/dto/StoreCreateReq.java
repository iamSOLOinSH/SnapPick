package com.sol.snappick.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Setter;

@Builder
@Schema(description = "스토어 생성 요청 DTO")
public class StoreCreateReq {

    @Schema(description = "스토어 이름")
    private String name;

    @Schema(description = "스토어 설명")
    private String description;

    @Schema(description = "스토어 위치")
    private String location;

    @Schema(description = "운영 시작 날짜")
    private LocalDate operateStartAt;

    @Schema(description = "운영 종료 날짜")
    private LocalDate operateEndAt;

    @Schema(description = "판매자 ID")
    private Integer sellerId;

    @Schema(description = "태그 목록")
    private List<String> tags;

    @Schema(description = "스토어 이미지 목록. 비워서 주세요")
    @Setter
    private List<StoreImageDto> images;

    @Schema(description = "운영 시간 목록")
    private List<StoreRunningTimeDto> runningTimes;
}
