package com.chris.framework.builder.annotation;

import java.lang.annotation.*;

/**
 * YuedaoXingApi
 * com.ydx.api.libs.chris.annotation
 * Created by Chris Chen
 * 2018/1/12
 * Explain:导入一个外部字段
 * 用户在一对一关系的两个对象中导入对方的字段数据
 * 字段数据并不直接从对象中获取，而是从提供者提供的方法中获取结果
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ImportField {
    Class<?> providerClass();//提供方法的类

    String method() default "";//提供数据的方法名，由于包装类相关的判断不够精确，所以建议给提供者取一个不同的名字
    //后面逻辑考虑还是用提供者注册比较好

    String keyField() default "id";//对象中用于发起调用的参数的取值字段名
}
