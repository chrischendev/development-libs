package com.chris.framework.builder.annotation.query;

import java.lang.annotation.*;

/**
 * YuedaoXingApi
 * com.ydx.api.libs.chris.annotation
 * Created by Chris Chen
 * 2018/1/12
 * Explain:查询一个外部数据对象的一个字段的值
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface QueryImportValue {
    Class<?> model();//对应外部数据类型 应该是ORM类

    String field() default "";//对应数据类型中的字段

    String key() default "";//对应查询基本数据类型ORM类中与外部数据关联的字段

    String FKey() default "id";//对应查询外部数据时外键关联的字段和基本数据类型ORM类中关联的字段，一般为主键id
}
