package com.chris.framework.builder.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * ChrisFrameworkObjectBuilder
 * com.chris.framework.builder.utils
 * Created by Chris Chen
 * 2018/1/17
 * Explain:开发工具
 */
public class DevTools {
    /**
     * 比较两个类的字段，把第二个类中有二第一个类中没有的字段返回
     *
     * @param clazz1
     * @param clazz2
     * @return
     */
    public static String[] compareFields(Class<?> clazz1, Class<?> clazz2) {
        List<String> fieldList = new ArrayList<>();
        Field[] fields1 = clazz1.getDeclaredFields();
        Field[] fields2 = clazz2.getDeclaredFields();

        for (Field field2 : fields2) {
            String field2Name = field2.getName();
            fieldList.add(field2Name);//先添加
            for (Field field1 : fields1) {
                String field1Name = field1.getName();
                if (field1Name.equals(field2Name)) {
                    fieldList.remove(field2Name);//找到移除
                    break;
                }
            }
        }
        String[] fieldNameArr = new String[fieldList.size()];

        return fieldList.toArray(fieldNameArr);
    }


}
