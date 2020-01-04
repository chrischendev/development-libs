package com.chris.framework.builder.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * MeiyueJavaProject
 * com.meiyue.library.utils
 * Created by Chris Chen
 * 2017/9/14
 * Explain:日志打印工具
 */
public class MsgUtils {

    public static void println(Object obj) {
        System.out.println(obj);
    }

    public static void print(Object obj) {
        System.out.print(obj);
    }

    /**
     * 输出列表
     *
     * @param objList
     */
    public static <T> void printList(List<T> objList) {
        for (T obj : objList) {
            println(JsonUtils.toJson(obj));
        }
    }

    /**
     * 输出Set
     *
     * @param objSet
     * @param <T>
     */
    public static <T> void printSet(Set<T> objSet) {
        for (T obj : objSet) {
            println(JsonUtils.toJson(obj));
        }
    }

    /**
     * 输出列表
     *
     * @param objMap
     */
    public static void printMap(Map<Object, Object> objMap) {
        for (Object key : objMap.keySet()) {
            println(objMap.get(key));
        }
    }

    public static void debug(Object obj) {
        if (true) {
            println(obj);
        }
    }


}
