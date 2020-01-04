package com.chris.framework.builder.annotation;

import java.lang.annotation.*;

/**
 * YuedaoXingApi
 * com.ydx.api.libs.chris.annotation
 * Created by Chris Chen
 * 2018/1/12
 * Explain:对象提供者方法唯一的查询参数
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ProviderParameter {
    String value() default "id";//唯一的一个查询参数名，默认为id
}
