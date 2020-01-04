package com.chris.framework.builder.annotation.persistence;

import java.lang.annotation.*;

/**
 * ChrisFrameworkObjectBuilder
 * com.chris.framework.builder.annotation
 * Created by Chris Chen
 * 2018/1/20
 * Explain:用以标注持久化方法的注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SaveMethod {
    Class<?> providerClass();//提供存储方法的类名

    String methodName() default "save";//存储方法名
}
