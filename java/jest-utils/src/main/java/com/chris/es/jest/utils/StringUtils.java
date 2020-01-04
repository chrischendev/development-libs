package com.chris.es.jest.utils;

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

    //判断参数值是否为空、空字符串或者全空格
    public static boolean isObjectEmpty(Object value) {
        //未传参数
        if (null == value) {
            return true;
        }
        if (value instanceof String) {
            //参数是空字符串
            if ("".equals(value)) {
                return true;
            }
            //参数全部都是空格
            if ("".equals(((String) value).trim())) {
                return true;
            }
        }
        return false;
    }
}
