package com.sol.snappick.global;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommonFormatter {
    private static final DecimalFormat formatter = new DecimalFormat("#,###");
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter yyyyMMdd_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static String yyyyMMddFormat(LocalDate date) {
        return date.format(yyyyMMdd_FORMAT);
    }

    public static String numberFormat(String number) {
        try {
            long value = Long.parseLong(number);
            return formatter.format(value) + "원";
        } catch (NumberFormatException e) {
            return number;
        }
    }

    public static String timeFormat(String time) {
        LocalDateTime date = LocalDateTime.parse(time, INPUT_FORMAT);
        return date.format(OUTPUT_FORMAT);
    }

    public static String timeFormat(LocalDateTime time) {
        return time.format(OUTPUT_FORMAT);
    }

}
