package com.sol.snappick.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.DayOfWeek;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Schema(description = "스토어 ")
public class StoreRunningTimeDto {

    private DayOfWeek dayOfWeek;

    private LocalTime startTime;

    private LocalTime endTime;
}
