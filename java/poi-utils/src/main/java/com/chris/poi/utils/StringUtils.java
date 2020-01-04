package com.chris.poi.utils;

/**
 * Created by Chris Chen
 * 2018/10/29
 * Explain:
 */

public class StringUtils {

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isEmptyOrAllBackSpace(String str) {
        return str == null || "".equals(str.trim());
    }

    public static boolean isNotEmptyOrAllBackSpace(String str) {
        return !isEmptyOrAllBackSpace(str);
    }
}
