package com.chris.framework.builder.annotation.persistence;

import java.lang.annotation.*;

/**
 * ChrisFrameworkObjectBuilder
 * com.chris.framework.builder.annotation
 * Created by Chris Chen
 * 2018/2/3
 * Explain:创建对象的字段
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@CreateField
@UpdateField
public @interface CuField {
    String value() default "";
}
