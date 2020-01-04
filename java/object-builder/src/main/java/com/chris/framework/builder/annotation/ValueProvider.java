package com.chris.framework.builder.annotation;

import java.lang.annotation.*;

/**
 * YuedaoXingApi
 * com.ydx.api.libs.chris.annotation
 * Created by Chris Chen
 * 2018/1/12
 * Explain:导出一个值
 * 用户在一对一关系的两个对象中导入对方的字段数据
 * 字段数据并不直接从对象中获取，而是从提供者提供的方法中获取结果
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ValueProvider {
    Class<?> externalClass();//关联的类

    String externalField();//与其他类关联的字段名，用这个字段能找到一个唯一的对象,用该字段获取值用于调用提供者获取数据

    String queryField() default "id";//对应在扩展数据类型中主要字段名，等同于数据库中的主键,主要是匹配调用
}
