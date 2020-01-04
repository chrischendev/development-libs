package com.chris.framework.builder.annotation;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.lang.annotation.*;

/**
 * YdxApiWebApp
 * com.ydx.app.annotation
 * Created by Chris Chen
 * 2018/4/6
 * Explain:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Mapper
@Repository
public @interface ChrisMapper {
    String value() default "";
}
