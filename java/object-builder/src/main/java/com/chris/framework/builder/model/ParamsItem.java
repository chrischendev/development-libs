package com.chris.framework.builder.model;

/**
 * ChrisFrameworkObjectBuilder
 * com.chris.framework.builder.model
 * Created by Chris Chen
 * 2018/1/23
 * Explain:参数
 */
public class ParamsItem {
    private Class<?> paramType;//参数类型
    private String name;//参数名
    private Object value;//参数值

    public ParamsItem() {
    }

    public ParamsItem(Class<?> paramType, String name, Object value) {
        this.paramType = paramType;
        this.name = name;
        this.value = value;
    }

    public Class<?> getParamType() {
        return paramType;
    }

    public void setParamType(Class<?> paramType) {
        this.paramType = paramType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
