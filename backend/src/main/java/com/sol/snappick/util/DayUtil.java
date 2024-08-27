package com.sol.snappick.util;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;

public class DayUtil {

    // 한글 요일을 영어 DayOfWeek로 매핑하는 함수
    public static DayOfWeek mapDayOfWeek(String day) {
        day = day.trim(); // 공백제거
        Map<String, DayOfWeek> dayOfWeekMap = new HashMap<>();
        dayOfWeekMap.put("월", DayOfWeek.MONDAY);
        dayOfWeekMap.put("화", DayOfWeek.TUESDAY);
        dayOfWeekMap.put("수", DayOfWeek.WEDNESDAY);
        dayOfWeekMap.put("목", DayOfWeek.THURSDAY);
        dayOfWeekMap.put("금", DayOfWeek.FRIDAY);
        dayOfWeekMap.put("토", DayOfWeek.SATURDAY);
        dayOfWeekMap.put("일", DayOfWeek.SUNDAY);

        return dayOfWeekMap.getOrDefault(day, null);
    }
}
