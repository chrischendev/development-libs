package com.chris.framework.builder.annotation;

import java.lang.annotation.*;

/**
 * YuedaoXingApi
 * com.ydx.api.libs.chris.annotation
 * Created by Chris Chen
 * 2018/1/12
 * Explain:展开字段
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ExpandField {
    String baseField();//对应在基本数据类型中的字段名

    String queryField() default "id";//对应在扩展数据类型中主要字段名，等同于数据库中的主键
}
