package com.chris.framework.builder.utils;

import com.chris.framework.builder.annotation.persistence.UpdateField;

import javax.persistence.Column;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * YuedaoXingApi
 * com.ydx.api.libs.utils
 * Created by Chris Chen
 * 2017/12/4
 * Explain:实体类工具
 */
public class EntityUtils {
    /**
     * 复制名称相同类型相同的字段数据
     * <p>
     * 功能：把源数据中和目标数据类型中名称相同，类型相匹配的字段名复制给一个新的目标数据类型的实例
     * 类型匹配：
     * 1.基本数据类型和包装类自动复制
     * 2.TimeStamp和Long、long长整型自动转换，忽略时区
     *
     * @param sourceObj 源数据对象
     * @param clazz     目标数据class
     * @param <T1>      源数据类型
     * @param <T2>      目标数据类型
     * @return 复制后的目标数据对象
     */
    public static <T1, T2> T2 copyData(T1 sourceObj, Class<T2> clazz) {
        if (sourceObj == null) {
            return null;
        }
        //2. 创建一个目标数据对象
        T2 targetObj = getInstance(clazz);
        if (targetObj == null) {
            return null;
        }
        //2. 复制两个对象相同的字段
        copyData(sourceObj, targetObj);
        return targetObj;
    }

    /**
     * 复制两个对象中相同字段的值
     * <p>
     * 功能：把源数据对象中和目标数据对象名称相同、类型相匹配的字段的值赋值给目标数据对象
     *
     * @param sourceObj 源数据对象
     * @param targetObj 目标数据对象
     * @param <T1>      源数据类型
     * @param <T2>      目标数据类型
     */
    public static <T1, T2> void copyData(T1 sourceObj, T2 targetObj) {
        //1. 获取两个对象的类
        Class<?> clazz1 = sourceObj.getClass();
        Class<?> clazz2 = targetObj.getClass();
        //3. 获取两个类字段集合
        Field[] fields1 = clazz1.getDeclaredFields();
        Field[] fields2 = clazz2.getDeclaredFields();
        //4. 遍历fields1
        for (Field f1 : fields1) {
            //4-1. 遍历fields2
            for (Field f2 : fields2) {
                //4-2. 复制字段
                copyFieldValue(sourceObj, targetObj, f1, f2);
            }
        }
    }

    /**
     * 根据字段列表复制字段的值
     * <p>
     * 功能：把源数据对象中与目标数据类型中名称相同且在字段列表中，类型还匹配的字段的值赋值给新创建的目标数据对象
     * 提示：及时类型匹配、名称相同，但是名称不在字段名列表中的字段是不会复制的
     *
     * @param sourceObj 源数据对象
     * @param clazz     目标数据class
     * @param fields    需要复制的字段名列表
     * @param <T1>      源数据类型
     * @param <T2>      目标数据类型
     * @return 目标数据对象
     */
    public static <T1, T2> T2 copyData(T1 sourceObj, Class<T2> clazz, String[] fields) {
        //1. 获取源数据的类
        Class<?> clazz1 = sourceObj.getClass();
        //2. 创建一个目标数据对象
        T2 targetObj = getInstance(clazz);
        if (targetObj == null) {
            return null;
        }
        //3. 获取两个类字段集合
        Field[] fields1 = clazz1.getDeclaredFields();
        Field[] fields2 = clazz.getDeclaredFields();
        //4. 复制字段
        copyFieldValue(sourceObj, targetObj, fields, fields1, fields2);
        return targetObj;
    }

    /**
     * 根据字段表复制两个对象中相同字段的值
     * <p>
     * 功能：把源数据对象中与目标数据对象中名称相同且在字段列表中，类型还匹配的字段的值赋值给的目标数据对象
     *
     * @param sourceObj 源数据对象
     * @param targetObj 目标数据对象
     * @param fields    需要复制的字段名称列表
     * @param <T1>      源数据类型
     * @param <T2>      目标数据类型
     */
    public static <T1, T2> void copyData(T1 sourceObj, T2 targetObj, String[] fields) {
        //1. 获取源两个对象的类
        Class<?> clazz1 = sourceObj.getClass();
        Class<?> clazz2 = targetObj.getClass();
        //3. 获取两个类字段集合
        Field[] fields1 = clazz1.getDeclaredFields();
        Field[] fields2 = clazz2.getDeclaredFields();
        //4. 复制字段
        copyFieldValue(sourceObj, targetObj, fields, fields1, fields2);
    }

    /**
     * 根据字段列表排除复制对象中相同字段的值
     * 体质：凡是在字段列表中出现的不进行复制
     *
     * @param sourceObj
     * @param clazz
     * @param fields
     * @param <T1>
     * @param <T2>
     * @return
     */
    public static <T1, T2> T2 copyDataExclude(T1 sourceObj, Class<T2> clazz, String[] fields) {
        //1. 获取源数据的类
        Class<?> clazz1 = sourceObj.getClass();
        //2. 创建一个目标数据对象
        T2 targetObj = getInstance(clazz);
        if (targetObj == null) {
            return null;
        }
        //3. 获取两个类字段集合
        Field[] fields1 = clazz1.getDeclaredFields();
        Field[] fields2 = clazz.getDeclaredFields();
        //4. 复制字段
        copyFieldValueExclude(sourceObj, targetObj, fields, fields1, fields2);
        return targetObj;
    }

    /**
     * 根据字段列表排除复制两个对象中相同字段的值
     * 提示：凡是在字段列表中出现的不进行复制
     *
     * @param sourceObj
     * @param targetObj
     * @param fields
     * @param <T1>
     * @param <T2>
     */
    public static <T1, T2> void copyDataExclude(T1 sourceObj, T2 targetObj, String[] fields) {
        //1. 获取源两个对象的类
        Class<?> clazz1 = sourceObj.getClass();
        Class<?> clazz2 = targetObj.getClass();
        //3. 获取两个类字段集合
        Field[] fields1 = clazz1.getDeclaredFields();
        Field[] fields2 = clazz2.getDeclaredFields();
        //4. 复制字段
        copyFieldValueExclude(sourceObj, targetObj, fields, fields1, fields2);
    }

    /**
     * 排除字段复制
     * 内部方法：
     * 逻辑：比较sourceObj和targetObj两个对象，获取两个对象的字段列表，只要是在fields字段名数组中出现过的字段就不会处理
     * 否则，机会把名称相同的字段进行复制
     *
     * @param sourceObj
     * @param targetObj
     * @param fields
     * @param fields1
     * @param fields2
     * @param <T1>
     * @param <T2>
     */
    private static <T1, T2> void copyFieldValueExclude(T1 sourceObj, T2 targetObj, String[] fields, Field[] fields1, Field[] fields2) {
        for (String fieldName : fields) {
            //4-1. 遍历fields1
            for (Field f1 : fields1) {
                //4-3. 是否匹配这个字段
                if (fieldName.equals(f1.getName())) {
                    //只要包含该字段就跳过
                    continue;
                }
                //4-4. 遍历fields2
                for (Field f2 : fields2) {
                    //4-5. 复制字段
                    copyFieldValue(sourceObj, targetObj, f1, f2);
                }

            }
        }
    }

    /**
     * 复制更新数据对象
     * 逻辑：把源数据对象中和目标数据对象同类同名并且有@UpdateField注解的字段复制过去
     * 本方法的两个对象原则上是同一个数据类型，不过也好似用于部分字段相同的不同数据类型
     *
     * @param sourceObj 请求中传递过来的修改数据
     * @param targetObj 从数据库中查询的修改前数据
     * @param <T1>
     * @param <T2>
     */
    public static <T1, T2> void copyUpdateObject(T1 sourceObj, T2 targetObj) {
        //1. 获取源两个对象的类
        Class<?> clazz1 = sourceObj.getClass();
        Class<?> clazz2 = targetObj.getClass();
        //2. 获取两个类字段集合
        Field[] fields1 = clazz1.getDeclaredFields();
        Field[] fields2 = clazz2.getDeclaredFields();
        //3. 遍历
        for (Field field1 : fields1) {
            //只有包含@UpdateField注解才进行比较操作
            if (field1.isAnnotationPresent(UpdateField.class)) {
                for (Field field2 : fields2) {
                    //进行名称和类型比较进行复制
                    copyFieldValue(sourceObj, targetObj, field1, field2);
                }
            }
        }
    }

    /**
     * 根据字段列表复制两个对象中相同字段的值
     * 内部方法：
     * 逻辑：如果两个字段名称相同就会进行复制
     *
     * @param sourceObj
     * @param targetObj
     * @param fields
     * @param fields1
     * @param fields2
     * @param <T1>
     * @param <T2>
     */
    private static <T1, T2> void copyFieldValue(T1 sourceObj, T2 targetObj, String[] fields, Field[] fields1, Field[] fields2) {
        for (String fieldName : fields) {
            //4-1. 遍历fields1
            for (Field f1 : fields1) {
                //4-3. 是否匹配这个字段
                if (fieldName.equals(f1.getName())) {
                    //4-4. 遍历fields2
                    for (Field f2 : fields2) {
                        //4-3. 是否匹配这个字段
                        if (fieldName.equals(f2.getName())) {
                            //4-2. 复制字段
                            copyFieldValue(sourceObj, targetObj, f1, f2);
                        }
                    }
                }
            }
        }
    }

    /**
     * 复制字段的值
     * 逻辑：比较两个对象的字段，如果类型相匹配，而且名称相同就会复制字段值
     * 类型匹配：
     * 1.基本数据类型和包装类自动复制
     * 2.TimeStamp和Long、long长整型自动转换，忽略时区
     *
     * @param sourceObj
     * @param targetObj
     * @param field1
     * @param field2
     * @param <T1>
     * @param <T2>
     */
    public static <T1, T2> void copyFieldValue(T1 sourceObj, T2 targetObj, Field field1, Field field2) {
        try {
            //1. 判断两个字段是否名称相同
            if (field1.getName().equals(field2.getName())) {
                //2. 获取源数据字段的值
                field1.setAccessible(true);
                field2.setAccessible(true);
                Object value = field1.get(sourceObj);
                if (equalFieldsType(field1, field2)) {
                    //2-1. 两个字段类型相同或者等同，直接赋值
                    field2.set(targetObj, value);
                } else if (timeStampEquals(field1, field2)) {
                    //特殊处理：TimeStamp和Long、longd的自动识别与转换
                    //2-2. 如果源是时间戳，目标是长整型，则取出time数值，赋值
                    field2.set(targetObj, value == null ? null : ((Timestamp) value).getTime());
                } else if (timeStampEquals(field2, field1)) {
                    //2-3. 如果源是长整型，目标是时间戳，则建立TimeStamp对象，赋值
                    field2.set(targetObj, value == null ? null : new Timestamp((Long) value));
                }
                //3. 访问权限还原
                field2.setAccessible(false);
                field1.setAccessible(false);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断TimeStamp和Long\long的匹配关系
     * 内部方法
     *
     * @param field1
     * @param field2
     * @return
     */
    private static boolean timeStampEquals(Field field1, Field field2) {
        if (field1 == null || field2 == null) {
            return false;
        }
        String timeStampName = Timestamp.class.getName();
        String fieldName1 = field1.getType().getName();
        String fieldName2 = field2.getType().getName();
        if (timeStampName.equals(fieldName1) &&
                (long.class.getName().equals(fieldName2) || Long.class.getName().equals(fieldName2))) {
            //如果前者为timestamp，而后者为long或者Long，则返回true
            return true;
        }
        return false;
    }

    /**
     * 获取一个泛型的实例
     * 内部方法
     *
     * @param clazz
     * @param <T>
     * @return
     */
    private static <T> T getInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            //e.printStackTrace();
        } catch (IllegalAccessException e) {
            //e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断两个字段的类型是否相同
     * 内部方法
     *
     * @param field1 复制源
     * @param field2 复制目标
     * @return
     */
    private static boolean equalFieldsType(Field field1, Field field2) {
        String fTypeName1 = field1.getType().getSimpleName();
        String fTypeName2 = field2.getType().getSimpleName();
        //1. 处理基本数据类型和包装类
        Map<String, String> map = new HashMap<String, String>();
        map.put(int.class.getSimpleName(), Integer.class.getSimpleName());
        map.put(byte.class.getSimpleName(), Byte.class.getSimpleName());
        map.put(short.class.getSimpleName(), Short.class.getSimpleName());
        map.put(char.class.getSimpleName(), Character.class.getSimpleName());
        map.put(long.class.getSimpleName(), Long.class.getSimpleName());
        map.put(float.class.getSimpleName(), Float.class.getSimpleName());
        map.put(double.class.getSimpleName(), Double.class.getSimpleName());
        map.put(boolean.class.getSimpleName(), Boolean.class.getSimpleName());

        /**
         * 在涉及包装类的判断逻辑中，源数据不能是包装类
         * 因为包装类一旦为null，会引发异常
         */
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            if (key.equals(fTypeName1) && map.get(key).equals(fTypeName2)) {
                return true;
            }
            if (key.equals(fTypeName2) && map.get(key).equals(fTypeName1)) {
                return true;
            }
        }
        //2. 名称相同、类型相同
        if (fTypeName1.equals(fTypeName2)) {
            return true;
        }
        return false;
    }

    /**
     * 把数据库查出来的结果集转换为目标对象
     * <p>
     * 用jdbc查询的结果集，根据持久化注解转换成实体类
     *
     * @param resultSet
     * @param entity    目标对象
     * @param <T>       目标类
     * @return
     */
    public static <T> T dataToEntity(ResultSet resultSet, T entity) {
        if (resultSet == null || entity == null) {
            return null;
        }
        //1. 获取结果集map
        Map rowData = null;
        try {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            rowData = new HashMap();
            for (int j = 1; j <= resultSetMetaData.getColumnCount(); j++) {
                rowData.put(resultSetMetaData.getColumnLabel(j), resultSet.getObject(j));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        //2. 获取entity的类
        Class<?> entityClass = entity.getClass();
        //3. 获取字段
        Field[] fields = entityClass.getDeclaredFields();
        //4. 遍历这些字段
        for (Field field : fields) {
            //5. 获取字段的@Column注解
            Column columnAnno = field.getDeclaredAnnotation(Column.class);
            if (columnAnno == null) {
                PropertyDescriptor pd = null;
                try {
                    //找到这个字段的getter方法
                    pd = new PropertyDescriptor(field.getName(), entityClass);
                    if (pd != null) {
                        Method readMethod = pd.getReadMethod();
                        if (readMethod != null) {
                            //获取getter方法上面的@Column
                            columnAnno = readMethod.getDeclaredAnnotation(Column.class);
                        }
                    }
                } catch (IntrospectionException e) {
                }
            }
            //6. 获取数据库表中的列名
            String colName = null;
            if (columnAnno != null) {
                colName = columnAnno.name();
            }
            if (StringUtils.isEmpty(colName)) {
                colName = field.getName();
            }
            //7. 获取map中的value
            Object value = rowData.get(colName);
            //8. 给字段赋值
            try {
                if (value != null) {
                    field.setAccessible(true);
                    field.set(entity, value);
                    field.setAccessible(false);
                }
            } catch (IllegalAccessException e) {
                continue;
            }
        }
        return entity;
    }

    /**
     * 结果接转换为目标类指定的对象
     *
     * @param resultSet
     * @param clazz     目标类
     * @param <T>
     * @return
     */
    public static <T> T dataToEntity(ResultSet resultSet, Class<T> clazz) {
        try {
            T t = clazz.newInstance();
            return dataToEntity(resultSet, t);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断两个类型是否相当（todo 暂时不可用）
     * 主要作用于基本数据类型和其包装类
     *
     * @param clazz1
     * @param clazz2
     * @return
     */
    public static boolean equalsType(Class<?> clazz1, Class<?> clazz2) {
        Class<?>[][] clazzArr = {
                {int.class, Integer.class},
                {short.class, Short.class},
                {long.class, Long.class},
                {float.class, Float.class},
                {double.class, Double.class},
                {char.class, Character.class},
                {byte.class, Byte.class},
                {boolean.class, Boolean.class}
        };

        String clazz1Name = clazz1.getName();
        String clazz2Name = clazz2.getName();
        for (int i = 0; i < clazzArr.length; i++) {
            String name0 = clazzArr[i][0].getName();
            String name1 = clazzArr[i][1].getName();
            if ((clazz1Name.equals(name0) && clazz2Name.equals(name1)) || (clazz1Name.equals(name1) && clazz2Name.equals(name0))) {
                return true;
            }
        }
        return false;
    }
}
