package com.chris.framework.builder.utils;

/**
 * ChrisFrameworkObjectBuilder
 * com.chris.framework.builder.utils
 * Created by Chris Chen
 * 2018/1/14
 * Explain:用于快速获取数据的工具类
 */
public class DataUtils {
    /**
     * 根据类型和主要字段的值获取对象
     * 这里主要字段名省略，表示按照"id"获取
     *
     * @param entityClazz
     * @param keyFieldValue
     * @param <T>
     * @return
     */
    public static <T> T getEntity(Class<T> entityClazz, Object keyFieldValue) {
        return getEntity(entityClazz, "id", keyFieldValue);
    }

    /**
     * 根据类型、主要字段名和值获取对象
     *
     * @param entityClazz
     * @param keyFieldName
     * @param keyFieldValue
     * @param <T>
     * @return
     */
    public static <T> T getEntity(Class<T> entityClazz, String keyFieldName, Object keyFieldValue) {
        //这里如果不判断是否为基本数据类型的话，是会出现空指针的，用ControllerUtils
        return (T) ExpandUtils.getObject(entityClazz, keyFieldName, keyFieldValue);
    }

//    public static <T> List<T> getList(Class<T> entityClazz) {
//
//    }
//
//    public static <T> List<T> getList(Class<T> entityClazz, String keyFieldName, Object keyFieldValue) {
//
//    }

}
