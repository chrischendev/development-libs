package com.chris.framework.builder.utils;

import com.chris.framework.builder.annotation.Expand;
import com.chris.framework.builder.annotation.query.Query;
import com.chris.framework.builder.model.Self;

import javax.persistence.Column;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * ChrisFrameworkObjectBuilder
 * com.chris.framework.builder.utils
 * Created by Chris Chen
 * 2018/1/16
 * Explain:处理类型的工具
 */
public class TypeUtils {
    /**
     * 判断一个类是不是基本数据类型或者其包装类
     *
     * @param clazz
     * @return
     */
    public static boolean equalsPrimitive(Class<?> clazz) {
        Class<?>[] clazzes = {
                int.class, Integer.class,
                short.class, Short.class,
                long.class, Long.class,
                float.class, Float.class,
                double.class, Double.class,
                byte.class, Byte.class,
                char.class, Character.class,
                boolean.class, Boolean.class
        };
        if (clazz == null) {
            return false;
        }
        String clazzName = clazz.getName();
        for (Class<?> cls : clazzes) {
            if (cls.getName().equals(clazzName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取基本数据类型
     *
     * @param object
     * @return
     */
    public static Class<?> getBaseEntityClass(Object object) {
        return getBaseEntityClass(object.getClass());
    }

    /**
     * 获取基本数据类型
     *
     * @param clazz
     * @return
     */
    public static Class<?> getBaseEntityClass(Class<?> clazz) {
        Expand expandAnno = clazz.getDeclaredAnnotation(Expand.class);
        if (expandAnno != null) {
            return expandAnno.baseEntity();
        }
        Query queryAnno = clazz.getDeclaredAnnotation(Query.class);
        if (queryAnno != null) {
            Class<?> baseClass = queryAnno.value();
            if (baseClass == null || baseClass.getName().equals(Self.class.getName())) {
                return clazz;
            }
            MsgUtils.println("baseClassName: " + baseClass.getName());
            return baseClass;
        }
        return clazz;
    }

    /**
     * 在一个对象中找到一个数据类型的字段
     *
     * @param object
     * @param fieldClass
     * @return
     */
    public static Field getFieldByTypeFromObject(Object object, Class<?> fieldClass) {
        Class<?> objectClass = object.getClass();
        Field[] fields = objectClass.getDeclaredFields();
        for (Field field : fields) {
            String fieldClassName = fieldClass.getName();
            String fieldTypeName = field.getType().getName();
            if (fieldClassName.equals(fieldTypeName)) {
                return field;
            }
        }
        return null;
    }

    /**
     * 在一个对象中找到一个特定名称的字段
     *
     * @param object
     * @param fieldName
     * @return
     */
    public static Field getFieldByNameFromObject(Object object, String fieldName) {
        Class<?> objectClass = object.getClass();
        return getFieldByNameFromClass(objectClass, fieldName);
    }

    /**
     * 在一个对象中找到一个特定名称的字段
     *
     * @param clazz
     * @param fieldName
     * @return
     */
    public static Field getFieldByNameFromClass(Class<?> clazz, String fieldName) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (fieldName.equals(field.getName())) {
                return field;
            }
        }
        return null;
    }

    /**
     * 根据类名获取类
     *
     * @param className
     * @return
     */
    public static Class<?> getClassForName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从一个基本数据类上面获取字段和数据库表列名的map
     *
     * @param baseClazz
     * @return
     */
    public static Map<String, com.chris.framework.builder.model.Column> getColumnNameMapFromBaseClass(Class<?> baseClazz) {
        if (baseClazz == null) {
            return null;
        }
        Map<String, com.chris.framework.builder.model.Column> colnumNameMap = new HashMap<>();
        //获取所有字段
        Field[] fields = baseClazz.getDeclaredFields();
        try {
            //遍历获取列名
            for (Field field : fields) {
                String fieldName = field.getName();
                String typeName = field.getType().getTypeName();
                PropertyDescriptor pd = new PropertyDescriptor(fieldName, baseClazz);
                Method readMethod = pd.getReadMethod();
                String colName1 = getColumnNameFromMethod(readMethod);
                //如果从getter方法上面找到了
                if (!StringUtils.isEmpty(colName1)) {
                    colnumNameMap.put(fieldName, new com.chris.framework.builder.model.Column(colName1, typeName));
                } else {
                    Method writeMethod = pd.getWriteMethod();
                    String colName2 = getColumnNameFromMethod(writeMethod);
                    //如果从setter方法上面找到了
                    if (!StringUtils.isEmpty(colName2)) {
                        colnumNameMap.put(fieldName, new com.chris.framework.builder.model.Column(colName2, typeName));
                    } else {
                        //获取
                        //如果getter和setter上面都没有，就取字段上面的拿到字段注解 todo 错了应该是拿到字段注解或者getter注解
                        Column column = field.getDeclaredAnnotation(Column.class);
                        if (column != null) {
                            String colName = column.name();
                            if (colName != null) {
                                colnumNameMap.put(fieldName, new com.chris.framework.builder.model.Column(colName, typeName));
                            }
                        }
                    }
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return colnumNameMap;
    }

    /**
     * 从一个基本数据类的方法上面获取数据库表的列名
     * 方法要带有@Colmun注解
     *
     * @param method
     * @return
     */
    private static String getColumnNameFromMethod(Method method) {
        //MsgUtils.println("getColName2==>methodName: " + method.getName());
        if (method != null) {
            Column annotation = method.getDeclaredAnnotation(Column.class);
            if (annotation != null) {
                String colName = annotation.name();
                if (!StringUtils.isEmpty(colName)) {
                    return colName;
                }
            }
        }
        return null;
    }

}
