package com.chris.framework.builder.utils;

import com.chris.framework.builder.annotation.Expand;
import com.chris.framework.builder.annotation.ExpandField;
import com.chris.framework.builder.annotation.ImportField;
import com.chris.framework.builder.core.exception.ProviderNotFoundException;
import com.chris.framework.builder.core.manager.ProviderFactory;
import com.chris.framework.builder.core.manager.ProviderObject;
import com.chris.framework.builder.core.manager.RegistryCenter;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * YuedaoXingApi
 * com.chris.framework.builder.utils
 * Created by Chris Chen
 * 2018/1/12
 * Explain:用户获取制定类型数据的工具
 */
public class ExpandUtils {
    /**
     * 一句基本数据baseObj，扩展为一个targetClass的实例
     *
     * @param baseObj
     * @param targetClass
     * @param <T1>
     * @param <T2>
     * @return
     */
    public static <T1, T2> T2 expand(T1 baseObj, Class<T2> targetClass) {
        if (baseObj == null) {
            return null;
        }
        //？？？如果扩展类和数据对象本就是同一类
        if (targetClass.getName().equals(baseObj.getClass().getName())) {
            return (T2) baseObj;
        }
        //1. 首先复制基本数据与目标数据类型相同而且名称相同的数据
        T2 targetObj = EntityUtils.copyData(baseObj, targetClass);
        //2. 获取targetClass中的所有字段
        Field[] fields = targetClass.getDeclaredFields();
        //3. 遍历这些字段
        for (Field field : fields) {
            //找到有@ExpandField注解的字段
            ExpandField expandFieldAnnotation = field.getDeclaredAnnotation(ExpandField.class);
            ////同样，如果这个字段的类型加了@ImportField注解，也不是一个基本数据，也应该调用处理
            ImportField importFieldnnotation = field.getDeclaredAnnotation(ImportField.class);
            //如果既没有符合要求的@ExpandField注解，也没有符合要求的@ImportField注解就忽略过
            //合法的逻辑是一个长度为0，一个长度为1，两者之和一定是1，否则就是不合格
            if (expandFieldAnnotation != null && importFieldnnotation != null) {
                continue;//如果没这两个注解都加上就忽略
            }
            if (expandFieldAnnotation == null && importFieldnnotation == null) {
                continue;//如果没有加这两个注解就忽略
            }
            Object valueObject = null;//字段最终的值对象
            //获取字段的类型,这个字段的类型就是获取字段扩展数据的类型
            Class<?> fieldClass = field.getType();/////必要参数:1 扩展数据类型////
            //如果注解为@ExpandField
            if (expandFieldAnnotation != null) {
                //获取@ExpandField注解的baseField值
                //baseField指的是在基本的类中对应的字段，也就是被展开的外键
                String baseField = expandFieldAnnotation.baseField();
                //在基本数据中找到这个字段，并且取得值
                Object fieldValue = getFieldValue(baseObj, baseField);/////必要参数:3 查询参数值////
                if (fieldValue == null) {
                    continue;//如果没有取到值就忽略
                }

                //获取用于在提供者中进行查询的参数名
                String queryField = expandFieldAnnotation.queryField();/////必要参数:2 查询字段名////

                //检查并处理List集合字段
                valueObject = expandObjectList(field, queryField, fieldValue);
                if (valueObject == null) {
                    //如果在这里没有取到值对象，就按照其他扩展规则去处理
                    ////如果这个字段的类型加了@Expand注解，也不是一个基本数据，也应该调用递归进行扩展
                    Expand expandAnnotation = fieldClass.getDeclaredAnnotation(Expand.class);
                    if (expandAnnotation != null) {//应该等于1，只允许一个
                        valueObject = getExpandValueObject(valueObject, fieldClass, fieldValue, queryField, expandAnnotation);
                    } else {
                        //有了目标类型targetClass。有了用于查找的参数名queryField和参数值fieldValue，可以去提供者工厂匹配提供者，获取对象
                        valueObject = getObject(fieldClass, queryField, fieldValue);//得到扩展对象
                    }
                }
            } else if (importFieldnnotation != null) {
                Object importValueObject = getImportValueObject(baseObj, importFieldnnotation);
                //这个值对象不一定是扩展后的，所以需要扩展
                //此处漏洞：基本数据类型和包装类
                if (importValueObject != null) {
                    String fieldClassName = fieldClass.getCanonicalName();
                    String importValueObjectClassName = importValueObject.getClass().getCanonicalName();
//                    String fieldClassName = fieldClass.getName();
//                    String importValueObjectClassName = importValueObject.getClass().getName();

                    if (TypeUtils.equalsPrimitive(fieldClass)
                            || fieldClassName.equals(String.class.getName())
                            || fieldClassName.equals(importValueObjectClassName)) {
                        //如果字段为基本数据类型或者字符串
                        valueObject = importValueObject;
                    } else {
                        valueObject = expand(importValueObject, fieldClass);
                    }
                }
            }

            if (valueObject == null) {
                continue;//同样，如果没有获得对象就忽略
            }
            //给字段field赋值
            field.setAccessible(true);//设置可访问
            try {
                field.set(targetObj, valueObject);//很有可能获得的对象和这个字段不匹配，程序员的毛病谁说得清
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                continue;//出现异常就忽略
            } finally {
                field.setAccessible(false);//设置不可以访问
            }
        }
        return targetObj;
    }

    /**
     * 如果这个字段的类型加了@Expand注解，也不是一个基本数据，也应该调用递归进行扩展
     *
     * @param valueObject
     * @param fieldClass
     * @param fieldValue
     * @param queryField
     * @param expandAnnotation1
     * @return
     */
    private static Object getExpandValueObject(Object valueObject, Class<?> fieldClass, Object fieldValue, String queryField, Expand expandAnnotation1) {
        Expand expandAnnotation = expandAnnotation1;
        //获取到基本数据的类型
        Class<?> baseEntityClass = expandAnnotation.baseEntity();
        //获取基本数据对象
        Object baseObject = getObject(baseEntityClass, queryField, fieldValue);
        if (baseObject != null) {
            //递归调用本方法，获取到最终的扩展对象
            valueObject = expand(baseObject, fieldClass);
        }
        return valueObject;
    }

    /**
     * 注解为@ImportField 获取值对象
     *
     * @param baseObj
     * @param importFieldnnotation
     * @param <T1>
     * @return
     */
    private static <T1> Object getImportValueObject(T1 baseObj, ImportField importFieldnnotation) {
        if (baseObj == null) {
            return null;
        }
        //获取关联字段
        Class<?> providerClass = importFieldnnotation.providerClass();
        String methodName = importFieldnnotation.method();
        String keyField = importFieldnnotation.keyField();
        Object value = getFiledValueFromObject(baseObj, keyField);
        if (value == null) {
            return null;
        }
        Object valueObject = getMethodReturnValue(providerClass, methodName, value);//得到值对象
        return valueObject;
    }

    /**
     * 按照字段名在对象中取得对应字段的值
     *
     * @param object    包含数据的对象
     * @param fieldName 字段名
     * @return
     */
    public static Object getFieldValue(Object object, String fieldName) {
        //1. 获得对象的Class
        Class<?> objectClass = object.getClass();
        //2. 获得所有字段
        Field[] fields = objectClass.getDeclaredFields();
        //3. 遍历
        for (Field field : fields) {
            //对照字段名，找到就取值收工
            if (fieldName.equals(field.getName())) {
                try {
                    field.setAccessible(true);
                    Object fieldValue = field.get(object);
                    return fieldValue;//返回值
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } finally {
                    field.setAccessible(false);
                }
            }
        }
        //4. 执行到这里，说明没找到
        return null;
    }

    /**
     * 根据返回数据类型，唯一参数名，参数值获取一个对象
     *
     * @param targetClass    贩返回数据类型
     * @param parameterName  唯一参数名
     * @param parameterValue 参数值
     * @return
     */
    public static Object getObject(Class targetClass, String parameterName, Object parameterValue) {
        //1. 根据返回数据类型和参数名匹配提供者
        ProviderObject provider = ProviderFactory.getProvider(targetClass, parameterName);
        return getObject(provider, parameterValue);
    }

    /**
     * 根据返回数据类型名称，唯一参数名，参数值获取一个对象
     *
     * @param targetClassName
     * @param parameterName
     * @param parameterValue
     * @return
     */
    public static Object getObject(String targetClassName, String parameterName, Object parameterValue) {
        //1. 根据返回数据类型和参数名匹配提供者
        ProviderObject provider = ProviderFactory.getProvider(targetClassName, parameterName);
        return getObject(provider, parameterValue);
    }

    /**
     * 根据提供者和参数值获取一个对象
     *
     * @param provider
     * @param parameterValue
     * @return
     */
    private static Object getObject(ProviderObject provider, Object parameterValue) {
        //如果没有找到匹配的提供者，则返回null
        if (provider == null) {
            try {
                throw new ProviderNotFoundException();//提供者没有找到
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //2. 取出必要数据
        Object object = provider.getObject();
        Method method = provider.getMethod();
        //3. 执行
        try {
            if (parameterValue == null) {
                return method.invoke(object);//返回获得的对象
            } else {
                return method.invoke(object, parameterValue);//返回获得的对象
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 处理List集合的扩展
     *
     * @param field      属性
     * @param queryField 用于查找数据的索引字段名，匹配提供者
     * @param fieldValue 参数值
     * @return
     */
    public static List expandObjectList(Field field, String queryField, Object fieldValue) {
        //1. 获取字段类型
        Class<?> fieldClass = field.getType();
        //1. 判断
        String fieldClassName = fieldClass.getName();
        if (!fieldClassName.equals(List.class.getName())) {
            return null;
        }
        //获取泛型
        Type genericType = field.getGenericType();
        if (genericType == null) {
            //...
        }
        String genericTypeTypeName = genericType.getTypeName();//List原来的整体数据类型

        if (!(genericType instanceof ParameterizedType)) {
            return null;
        }

        ParameterizedType pt = (ParameterizedType) genericType;
        Class<?> genericClass = (Class) pt.getActualTypeArguments()[0];
        String typeName = genericClass.getName();//List包含的泛型类

        //获取这个泛型类的注解
        Expand[] expandAnnotations = genericClass.getAnnotationsByType(Expand.class);
        if (expandAnnotations == null || expandAnnotations.length == 0) {
            return null;
        }
        Expand expandAnnotation = expandAnnotations[0];
        //获取其基本类
        Class<?> baseEntityClass = expandAnnotation.baseEntity();
        String baseEntityClassName = baseEntityClass.getName();//基本数据类型
        //旧的类名fieldClassName,扩展泛型类名typeName,基本数据类名baseEntityClassName,执行替换
        String baseObjectListClassName = genericTypeTypeName.replace(typeName, baseEntityClassName);//替换后
        //获取集合数据
        List<Object> baseObjectList = (List<Object>) getObject(baseObjectListClassName, queryField, fieldValue);
        Class<?> expandClass = null;
        try {
            expandClass = Class.forName(typeName);
            return expandObjectList(baseObjectList, expandClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取一个基本数据类型的List
     * 目前只允许一个参数，可以为空
     *
     * @param <T>
     * @return
     */
    public static <T> List<T> getBaseEntityList(Class<T> baseClass, String paramName, Object paramValue) {
        //构建整体类名
        String baseObjectListClassName = new StringBuilder(List.class.getName())
                .append("<")
                .append(baseClass.getName())
                .append(">")
                .toString();
        //获取集合数据
        List<T> baseEntityList = (List<T>) getObject(baseObjectListClassName, paramName, paramValue);
        return baseEntityList;
    }

    /**
     * 扩展List数据集合
     *
     * @param sourceList  源数据集合
     * @param targetClass 目标数据集合泛型
     * @param <T1>
     * @param <T2>
     * @return
     */
    public static <T1, T2> List<T2> expandList(List<T1> sourceList, Class<T2> targetClass) {
        return expandObjectList(sourceList, targetClass);
    }

    private static List expandObjectList(List sourceList, Class<?> targetClass) {
        //1. 如果传进类的集合为空或者没有元素，则返回null
        if (sourceList == null || sourceList.size() == 0) {
            return null;
        }
        //2. 遍历
        List targetList = new ArrayList();
        for (Object sourceObj : sourceList) {
            targetList.add(expand(sourceObj, targetClass));
        }
        return targetList;
    }

    /**
     * 根据类型从一个对象中获取一个实例化的对象
     *
     * @param objects
     * @param fieldClass
     * @return
     */
    public static Object getFieldObjectFromObject(Set<Object> objects, Class<?> fieldClass) {
        if (objects == null || objects.size() == 0) {
            return null;
        }
        for (Object object : objects) {
            //1. 获得类
            Class<?> objectClass = object.getClass();
            //2. 获得所有属性
            Field[] fields = objectClass.getDeclaredFields();
            //3. 遍历属性
            for (Field field : fields) {
                //只要找到一个就返回对象
                if (field.getType().getName().equals(fieldClass.getName())) {
                    try {
                        return field.get(object);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 调用一个方法，获得返回结果
     *
     * @param providerClass
     * @param methodName
     * @param parameterValue
     * @return
     */
    public static Object getMethodReturnValue(Class<?> providerClass, String methodName, Object parameterValue) {
        //1. 获取实体类
        Set<Object> providerBoxs = RegistryCenter.getProviderBoxs();
        if (providerBoxs == null || providerBoxs.size() == 0) {
            return null;
        }
        Object object = getFieldObjectFromObject(providerBoxs, providerClass);
        if (object == null) {
            return null;
        }
        //2. 获取方法 todo 包装类匹配有偏差，所以用遍历，只匹配第一个方法名相同的方法，待完善
        /*
        try {
            Method method = providerClass.getDeclaredMethod(methodName, parameterValue.getClass());
            //3. 调用获取返回值
            return method.invoke(object, parameterValue);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        */

        Method[] methods = providerClass.getDeclaredMethods();
        if (methods == null || methods.length == 0) {
            return null;
        }
        for (Method method : methods) {
            if (methodName.equals(method.getName())) {
                try {
                    return method.invoke(object, parameterValue);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 获取对象中一个字段的值
     *
     * @param object
     * @param fieldName
     * @return
     */
    public static Object getFiledValueFromObject(Object object, String fieldName) {
        if (object == null || StringUtils.isEmpty(fieldName)) {
            return null;
        }
        Class<?> objectClass = object.getClass();
        //2. 获取方法
        try {
            Field field = objectClass.getDeclaredField(fieldName);
            //3. 获取返回值
            field.setAccessible(true);
            Object value = field.get(object);
            field.setAccessible(false);
            return value;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
