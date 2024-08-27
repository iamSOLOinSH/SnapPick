package com.sol.snappick.store.mapper;

import com.sol.snappick.store.dto.StoreRunningTimeDto;
import com.sol.snappick.store.entity.Store;
import com.sol.snappick.store.entity.StoreRunningTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StoreRunningTimeMapper {

    default List<StoreRunningTime> toEntityList(
        List<StoreRunningTimeDto> runningTimeDtos,
        Store store
    ) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        if (runningTimeDtos == null || store == null) {
            return List.of(); // 빈 리스트를 반환하여 NPE 를 방지
        }
        return runningTimeDtos.stream()
                              .map(dto -> StoreRunningTime.builder()
                                                          .dayOfWeek(dto.getDayOfWeek())
                                                          .startTime(
                                                              LocalTime.parse(dto.getStartTime(),
                                                                              timeFormatter
                                                              ))
                                                          .endTime(LocalTime.parse(dto.getEndTime(),
                                                                                   timeFormatter
                                                          ))
                                                          .store(store)
                                                          .build())
                              .toList();
    }
}
