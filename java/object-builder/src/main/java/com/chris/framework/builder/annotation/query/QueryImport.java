package com.chris.framework.builder.annotation.query;

import java.lang.annotation.*;

/**
 * YuedaoXingApi
 * com.ydx.api.libs.chris.annotation
 * Created by Chris Chen
 * 2018/1/12
 * Explain:查询一个外部数据对象数据
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface QueryImport {
    Class<?> model();//对应数据类型 应该是ORM类

    String keyField() default "";//对应基本数据类型中的字段，即外键

    String FKey() default "id";//外键关联的类的主键，即外键关联
}
