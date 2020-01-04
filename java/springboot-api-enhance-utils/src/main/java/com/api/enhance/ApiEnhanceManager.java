package com.api.enhance;

import java.util.HashMap;
import java.util.Map;

/**
 * Create by Chris Chan
 * Create on 2019/4/1 11:02
 * Use for:
 */
public class ApiEnhanceManager {
    private static Map<String, Long> timeLimitMap = new HashMap<>();

    public static Map<String, Long> getTimeLimitMap() {
        return timeLimitMap;
    }

    public static void setTimeLimitMap(Map<String, Long> map) {
        ApiEnhanceManager.timeLimitMap = map;
    }

    public static void addTimeLimit(Map.Entry<String, Long> timeLimitEntry) {
        if (ApiEnhanceManager.timeLimitMap == null) {
            ApiEnhanceManager.timeLimitMap = new HashMap<>();
        }
        ApiEnhanceManager.timeLimitMap.put(timeLimitEntry.getKey(), timeLimitEntry.getValue());
    }

    public static void addTimeLimit(String key, Long time) {
        if (ApiEnhanceManager.timeLimitMap == null) {
            ApiEnhanceManager.timeLimitMap = new HashMap<>();
        }
        ApiEnhanceManager.timeLimitMap.put(key, time);
    }

    public static Long getTimeLimit(String key) {
        if (ApiEnhanceManager.timeLimitMap == null) {
            return null;
        }
        return ApiEnhanceManager.timeLimitMap.get(key);
    }
}
