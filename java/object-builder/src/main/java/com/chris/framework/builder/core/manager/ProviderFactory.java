package com.chris.framework.builder.core.manager;

import com.chris.framework.builder.utils.MsgUtils;
import com.chris.framework.builder.utils.StringUtils;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * YuedaoXingApi
 * com.ydx.api.libs.chris.core
 * Created by Chris Chen
 * 2018/1/12
 * Explain:提供者工厂
 * 为方便起见，决定设置一个Springboot bean对象的专用文件
 * 注册中心初始化时，就在这些专用文件下寻找对象，并且扫描做了提供者注解的方法进行注册
 */
public class ProviderFactory {
    private static Set<ProviderObject> providerObjectSet = new HashSet<>();
    private static Set<ProviderClassObject> providerClassObjectSet = new HashSet<>();

    /**
     * 添加一个提供者方法
     *
     * @param providerObject
     */
    public static void addProvider(ProviderObject providerObject) {
        if (providerObject == null || providerObjectSet.contains(providerObject)) {
            return;
        }
        providerObjectSet.add(providerObject);
    }

    /**
     * 根据返回数据类型和唯一参数名匹配一个提供者
     *
     * @param targetClass
     * @param parameterName
     * @return
     */
    public static ProviderObject getProvider(Class targetClass, String parameterName) {
        if (targetClass == null) {
            return null;
        }
        String targetClassName = targetClass.getName();
        return getProvider(targetClassName, parameterName);
    }

    /**
     * 根据返回数据类型名和唯一参数名匹配一个提供者
     *
     * @param targetClassName
     * @param parameterName
     * @return
     */
    public static ProviderObject getProvider(String targetClassName, String parameterName) {
        for (ProviderObject provider : providerObjectSet) {
            //找到就休息（这里的类名要用全名），今天累了，没有想好如何匹配参数类型
            Type returnClass = provider.getReturnClass();
            String returnClassName = returnClass.getTypeName();
            //判断returnClassName如果是List，就要用另外的方法获取名称
            if (returnClassName.equals(List.class.getName())) {
                returnClassName = provider.getMethod().getGenericReturnType().getTypeName();
            }
            //MsgUtils.println(returnClassName + " -:- " + provider.getParameterType() + " -:- " + provider.getParameter());

            if (targetClassName.equals(returnClassName)) {
                //MsgUtils.println("返回类型匹配： " + returnClassName + " : " + targetClassName);
                // 如果 parameterName为空，则匹配参数名为空的提供者
                if (StringUtils.isEmpty(parameterName) && StringUtils.isEmpty(provider.getParameter())) {
                    //MsgUtils.println("参数名都为 空： " + provider.getParameter() + " : " + parameterName);
                    return provider;
                }
                // 如果参数名（目前一个参数）相同，则匹配
                if (parameterName != null && parameterName.equals(provider.getParameter())) {
                    //MsgUtils.println("参数名相同： " + provider.getParameter() + " : " + parameterName);
                    return provider;
                }
            }
        }
        return null;
    }

    public static Set<ProviderObject> getProviderObjectSet() {
        return providerObjectSet;
    }

    /**
     * 添加一个提供者类
     *
     * @param providerClassObject
     */
    public static void addProviderClass(ProviderClassObject providerClassObject) {
        if (providerClassObject == null || providerObjectSet.contains(providerClassObject)) {
            return;
        }
        providerClassObjectSet.add(providerClassObject);
    }

    /**
     * 根据类型找到一个提供者
     *
     * @param providerClass
     * @return
     */
    public static ProviderClassObject getProviderClass(Class<?> providerClass) {
        if (providerClass == null) {
            return null;
        }
        return getProviderClass(providerClass.getName());
    }

    /**
     * 根据类型名找到一个提供者
     *
     * @param providerClassName
     * @return
     */
    public static ProviderClassObject getProviderClass(String providerClassName) {
        if (StringUtils.isEmpty(providerClassName)) {
            return null;
        }
        for (ProviderClassObject providerClass : providerClassObjectSet) {
            String name = providerClass.getProviderClassName();
            if (providerClassName.equals(name)) {
                return providerClass;
            }
        }
        return null;
    }

    public static Set<ProviderClassObject> getProviderClassObjectSet() {
        return providerClassObjectSet;
    }
}
