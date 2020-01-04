package com.chris.framework.builder.core.manager;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * YuedaoXingApi
 * com.ydx.api.libs.chris.core
 * Created by Chris Chen
 * 2018/1/12
 * Explain:注册在提供者工厂中的提供者
 * 工厂中的提供者集合Set，用提供者方法返回的数据类型作为key
 */
public class ProviderObject {
    private Type returnClass;//提供者返回的数据类型 ps：传类型名需要全名太麻烦，不如传一个class
    private Object object;//包含提供者方法的对象
    private Method method;//提供者方法 这是主体
    private Type parameterType;//提供者需要提供的参数类型 目前设置一个 扩展思路：通过参数表匹配相同返回类型的提供者
    private String parameter;//提供者需要提供的参数 目前设置一个 扩展思路：通过参数表匹配相同返回类型的提供者

    public ProviderObject() {
    }

    public ProviderObject(Type returnClass, Object object, Method method, Type parameterType, String parameter) {
        this.returnClass = returnClass;
        this.object = object;
        this.method = method;
        this.parameterType = parameterType;
        this.parameter = parameter;
    }

    public ProviderObject(Type returnClass, Method method, Type parameterType, String parameter) {
        this.returnClass = returnClass;
        this.method = method;
        this.parameterType = parameterType;
        this.parameter = parameter;
    }


    public Type getReturnClass() {
        return returnClass;
    }

    public void setReturnClass(Type returnClass) {
        this.returnClass = returnClass;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Type getParameterType() {
        return parameterType;
    }

    public void setParameterType(Type parameterType) {
        this.parameterType = parameterType;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    @Override
    public String toString() {
        return "ProviderObject{" +
                "returnClass=" + returnClass +
                ", object=" + object +
                ", method=" + method +
                ", parameterTypes=" + parameterType;
    }
}
