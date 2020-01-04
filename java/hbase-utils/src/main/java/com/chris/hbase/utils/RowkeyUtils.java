package com.chris.hbase.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ntp.TimeStamp;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Create by Chris Chen
 * Create on 2019-03-08 10:08 星期五
 * This class be use for: 行键构建工具
 */
public class RowkeyUtils {
    public static String buildRowkeyDefault(Object... params) {
        return buildRowkey("_", params);
    }

    public static String buildRowkey(String linkSymbol, Object... params) {
        StringBuilder sb = new StringBuilder();
        for (Object param : params) {
            if (param instanceof TimeStamp) {
                sb.append(((Timestamp) param).getTime()).append(linkSymbol);
            } else if (param instanceof Date) {
                sb.append(((Date) param).getTime()).append(linkSymbol);
            } else {
                sb.append(param).append(linkSymbol);
            }
        }
        return sb.replace(sb.lastIndexOf(linkSymbol), sb.length(), "").toString();
    }

    /**
     * 按照对象的字段名列表构建rowkey
     * 默认连接符 -
     * @param data
     * @param fieldNames
     * @param <T>
     * @return
     */
    public static <T> String buildDefult(T data, String... fieldNames) {
        return build(data, null, fieldNames);
    }

    /**
     * 按照对象的字段名列表构建rowkey
     *
     * @param data
     * @param linkSymbol
     * @param fieldNames
     * @param <T>
     * @return
     */
    public static <T> String build(T data, String linkSymbol, String... fieldNames) {
        if (null == data || null == fieldNames || fieldNames.length == 0) {
            return null;
        }
        if (StringUtils.isBlank(linkSymbol)) {
            linkSymbol = "-";
        }
        StringBuilder sb = new StringBuilder();
        Class<?> dataClass = data.getClass();
        try {
            for (String fieldName : fieldNames) {
                Field field = dataClass.getField(fieldName);//获取字段
                field.setAccessible(true);
                Object value = field.get(data);//取值
                if (Timestamp.class.getName().equals(field.getType().getTypeName())) {
                    long time = ((Timestamp) value).getTime();
                    sb.append(time).append(linkSymbol);
                } else if (Date.class.getName().equals(field.getType().getTypeName())) {
                    sb.append(((Date) value).getTime()).append(linkSymbol);
                } else {
                    sb.append(value).append(linkSymbol);
                }
                field.setAccessible(false);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        sb.replace(sb.lastIndexOf(linkSymbol), sb.length(), "");
        return sb.toString();
    }

    /**
     * 根据对象类属性的索引顺序构建rowkey
     *
     * @param data
     * @param linkSymbol
     * @param fieldIndexs
     * @param <T>
     * @return
     */
    public static <T> String buildByFieldIndexs(T data, String linkSymbol, int... fieldIndexs) {
        if (null == data || null == fieldIndexs || fieldIndexs.length == 0) {
            return null;
        }
        if (StringUtils.isBlank(linkSymbol)) {
            linkSymbol = "-";
        }
        StringBuilder sb = new StringBuilder();
        Class<?> dataClass = data.getClass();
        Field[] fields = dataClass.getFields();
        try {
            for (int index : fieldIndexs) {
                Field field = fields[index];
                field.setAccessible(true);
                Object value = field.get(data);//取值
                if (TimeStamp.class.getName().equalsIgnoreCase(field.getType().getTypeName())) {
                    sb.append(((Timestamp) value).getTime()).append(linkSymbol);
                } else if (Date.class.getName().equalsIgnoreCase(field.getType().getTypeName())) {
                    sb.append(((Date) value).getTime()).append(linkSymbol);
                } else {
                    sb.append(value).append(linkSymbol);
                }
                field.setAccessible(false);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        sb.replace(sb.lastIndexOf(linkSymbol), sb.length(), "");
        return sb.toString();
    }

    /**
     * 获取一个类公有方法的集合
     *
     * @param clazz
     * @return
     */
    public static String[] getFieldsFromClass(Class<?> clazz) {
        if (null == clazz) {
            return null;
        }
        Field[] fields = clazz.getFields();
        int length = fields.length;
        String[] fieldNames = new String[length];
        for (int i = 0; i < length; i++) {
            fieldNames[i] = fields[i].getName();
        }
        return fieldNames;
    }

}
