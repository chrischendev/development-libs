package com.chris.framework.builder.core.manager;

/**
 * ChrisFrameworkObjectBuilder
 * com.chris.framework.builder.core
 * Created by Chris Chen
 * 2018/1/20
 * Explain:提供者类对象
 */
public class ProviderClassObject {
    private String providerClassName;//提供者类
    private Object providerObject;//提供者实例

    public ProviderClassObject() {
    }

    public ProviderClassObject(String providerClassName, Object providerObject) {
        this.providerClassName = providerClassName;
        this.providerObject = providerObject;
    }

    public String getProviderClassName() {
        return providerClassName;
    }

    public void setProviderClassName(String providerClassName) {
        this.providerClassName = providerClassName;
    }

    public Object getProviderObject() {
        return providerObject;
    }

    public void setProviderObject(Object providerObject) {
        this.providerObject = providerObject;
    }
}
