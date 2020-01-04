package com.chris.framework.builder.utils;

/**
 * ChrisFrameworkObjectBuilder
 * com.chris.framework.builder.utils
 * Created by Chris Chen
 * 2018/1/16
 * Explain:处理字符串的工具类
 */
public class StringUtils {
    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str)) {
            return true;
        }
        return false;
    }

    /**
     * 获得小驼峰格式
     *
     * @param str
     * @return
     */
    public static String getLowerCamel(String str) {
        int i = str.indexOf("_");
        char c = str.charAt(i + 1);
        str = str.replace("_" + c, String.valueOf(c).toUpperCase());
        if (i < 0) {
            return lowerFirstLetter(str);
        }
        return getLowerCamel(str);
    }

    /**
     * 获得大驼峰格式
     *
     * @param str
     * @return
     */
    public static String getUpperCamel(String str) {
        return upperFirstLetter(getLowerCamel(str));
    }

    /**
     * 首字母小写
     *
     * @param str
     * @return
     */
    public static String lowerFirstLetter(String str) {
        String firstLetter = String.valueOf(str.charAt(0));
        return firstLetter.toLowerCase() + str.substring(1);
    }

    /**
     * 首字母大写
     *
     * @param str
     * @return
     */
    public static String upperFirstLetter(String str) {
        String firstLetter = String.valueOf(str.charAt(0));
        return firstLetter.toUpperCase() + str.substring(1);
    }

    /**
     * 根据一个完整的类名称获取简化部分的类型名
     * 就是从最后一个.后面截取
     *
     * @param fullClassName
     * @return
     */
    public static String getSimpleClassNameFromFullClassName(String fullClassName) {
        return fullClassName.substring(fullClassName.lastIndexOf(".")+1);
    }
}
