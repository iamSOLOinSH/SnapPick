package com.sol.snappick.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.DayOfWeek;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "스토어 운영시간 DTO")
public class StoreRunningTimeDto {

    @Schema(description = "요일")
    private DayOfWeek dayOfWeek;

    @Schema(description = "요일별 운영 시작 시간")
    private String startTime;

    @Schema(description = "요일별 운영 종료 시간")
    private String endTime;

    @Builder
    public StoreRunningTimeDto(
        DayOfWeek dayOfWeek,
        String startTime,
        String endTime
    ) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
