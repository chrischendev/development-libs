package com.api.enhance.annotation;

import java.lang.annotation.*;

/**
 * Create by Chris Chan
 * Create on 2019/4/1 10:37
 * Use for: 连续访问时间限制
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface TimeLimit {
    long value() default 0;
}
