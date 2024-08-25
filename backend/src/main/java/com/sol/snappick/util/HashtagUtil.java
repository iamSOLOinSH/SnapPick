package com.sol.snappick.util;

import java.util.Arrays;
import java.util.List;

public class HashtagUtil {
    public static List<String> convertToList(String hashTagString) {
        if(hashTagString != null && !hashTagString.isEmpty()) {
            return Arrays.asList(hashTagString.split(","));
        }
        return List.of();
    }
}
