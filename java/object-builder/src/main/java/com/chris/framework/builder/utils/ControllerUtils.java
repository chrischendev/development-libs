package com.chris.framework.builder.utils;

import com.chris.framework.builder.annotation.Expand;
import com.chris.framework.builder.model.ParamsItem;

import java.lang.reflect.Field;
import java.util.List;

/**
 * ChrisFrameworkObjectBuilder
 * com.chris.framework.builder.utils
 * Created by Chris Chen
 * 2018/1/23
 * Explain:接口处的工具
 */
public class ControllerUtils {
    /**
     * 根据返回类型和参数体获取最终对象
     *
     * @param clazz
     * @param params 参数表 不能具有复杂的层次 属性都必须是值对象
     * @param <T>
     * @return
     */
    public static <T> T get(Class<T> clazz, Object params) {
        if (clazz == null) {
            return null;
        }
        //构建参数表
        ParamsItem[] paramsItems = getParamItemsByParamsObject(params);
        //1. 获取clazz的@Expand注解
        Expand expandAnno = clazz.getDeclaredAnnotation(Expand.class);
        //2. 如果没有@Expand这个注解，则将其视为基本数据类型
        if (expandAnno == null) {
            ParamsItem paramsItem = paramsItems[0];
            if (paramsItem != null) {
                //目前封装的获取基本数据类型都要提供至少一个参数，默认为id
                return (T) ExpandUtils.getObject(clazz, paramsItem.getName(), paramsItem.getValue());
            }
        }
        //3. 否则就执行扩展（todo 这里的写法和上面一样，因为目前封装的参数都是一个，将来会增强）
        Class<?> baseEntityClass = expandAnno.baseEntity();
        if (baseEntityClass == null) {
            return null;
        }
        ParamsItem paramsItem = paramsItems[0];
        if (paramsItem != null) {
            Object baseEntity = ExpandUtils.getObject(baseEntityClass, paramsItem.getName(), paramsItem.getValue());
            return ExpandUtils.expand(baseEntity, clazz);
        }
        return null;
    }

    /**
     * 获取并且扩展一个集合
     *
     * @param clazz
     * @param params
     * @param <T>
     * @return
     */
    public static <T> List<T> getList(Class<T> clazz, Object params) {
        if (clazz == null) {
            return null;
        }
        //构建参数表
        ParamsItem[] paramsItems = getParamItemsByParamsObject(params);
        //1. 获取clazz的@Expand注解
        Expand expandAnno = clazz.getDeclaredAnnotation(Expand.class);
        //2. 如果没有@Expand这个注解，则将其视为基本数据类型
        if (expandAnno == null) {
            ParamsItem paramsItem = paramsItems[0];
            if (paramsItem != null) {
                //目前封装的获取基本数据类型都要提供至少一个参数，默认为id
                return ExpandUtils.getBaseEntityList(clazz, paramsItem.getName(), paramsItem.getValue());
            }
        }
        //3. 否则就执行扩展
        Class<?> baseEntityClass = expandAnno.baseEntity();
        if (baseEntityClass == null) {
            return null;
        }
        ParamsItem paramsItem = paramsItems[0];
        if (paramsItem != null) {
            List<?> baseEntityList = ExpandUtils.getBaseEntityList(baseEntityClass, paramsItem.getName(), paramsItem.getValue());
            return ExpandUtils.expandList(baseEntityList, clazz);
        }
        return null;
    }

    /**
     * 获取并且扩展一个集合，无参数
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> getList(Class<T> clazz) {
        if (clazz == null) {
            return null;
        }
        //1. 获取clazz的@Expand注解
        Expand expandAnno = clazz.getDeclaredAnnotation(Expand.class);
        //2. 如果没有@Expand这个注解，则将其视为基本数据类型
        if (expandAnno == null) {
            //目前封装的获取基本数据类型都要提供至少一个参数，默认为id
            return ExpandUtils.getBaseEntityList(clazz, null, null);//provider的默认参数名为id
        }
        //3. 否则就执行扩展
        Class<?> baseEntityClass = expandAnno.baseEntity();
        if (baseEntityClass == null) {
            return null;
        }
        List<?> baseEntityList = ExpandUtils.getBaseEntityList(baseEntityClass, null, null);
        return ExpandUtils.expandList(baseEntityList, clazz);
    }

    /**
     * 把一个参数表对象转换成为一个参数表
     *
     * @param params
     * @return
     */
    private static ParamsItem[] getParamItemsByParamsObject(Object params) {
        if (params == null) {
            return null;
        }
        //1. 获取params的属性数组
        Field[] fields = params.getClass().getDeclaredFields();
        //2. 新建参数数组
        int length = fields.length;
        ParamsItem[] paramsItems = new ParamsItem[length];
        //3. 遍历属性数组
        for (int i = 0; i < length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            try {
                ParamsItem paramsItem = new ParamsItem(field.getType(), field.getName(), field.get(params));
                paramsItems[i] = paramsItem;
            } catch (IllegalAccessException e) {
                //e.printStackTrace();
                continue;
            }
            field.setAccessible(false);
        }
        return paramsItems;
    }
}
