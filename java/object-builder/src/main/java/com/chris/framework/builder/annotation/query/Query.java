package com.chris.framework.builder.annotation.query;

import com.chris.framework.builder.model.Self;

import java.lang.annotation.*;

/**
 * YuedaoXingApi
 * com.ydx.api.libs.chris.annotation
 * Created by Chris Chen
 * 2018/1/12
 * Explain:查询数据参考的ORM类
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Query {
    Class<?> value() default Self.class;//对应数据类型 应该是ORM类 默认为Self，表示查询参照基本类就是自己
}
