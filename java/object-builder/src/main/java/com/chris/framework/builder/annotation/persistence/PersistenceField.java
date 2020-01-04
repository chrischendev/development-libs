package com.chris.framework.builder.annotation.persistence;

import java.lang.annotation.*;

/**
 * YuedaoXingApi
 * com.ydx.api.libs.chris.annotation
 * Created by Chris Chen
 * 2018/1/12
 * Explain:用于将对象填入持久化对象
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface PersistenceField {
    Class<?> baseEntity();//对应基本数据类型

    String keyField() default "";//基本数据类型中的对应字段，即外键，一般有这个注解，则后面的fieldName被忽略

    //String fieldName() default "";//对应基本数据类型中的对应字段 目前看来没意义
}
