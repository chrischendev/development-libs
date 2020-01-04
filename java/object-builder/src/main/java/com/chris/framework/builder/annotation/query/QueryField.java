package com.chris.framework.builder.annotation.query;

import java.lang.annotation.*;

/**
 * YuedaoXingApi
 * com.ydx.api.libs.chris.annotation
 * Created by Chris Chen
 * 2018/1/12
 * Explain:查询字段
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface QueryField {
    String field() default "";//对应基本数据类型的字段名

    QueryCompare compare() default QueryCompare.EQUAL;//比较方式，默认 等于
}
