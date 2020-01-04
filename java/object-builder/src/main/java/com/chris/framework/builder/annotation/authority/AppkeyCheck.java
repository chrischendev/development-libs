package com.chris.framework.builder.annotation.authority;

import java.lang.annotation.*;

/**
 * YuedaoApi
 * com.yuedao.library.annotation
 * Created by Chris Chen
 * 2017/9/20
 * Explain:验证appkey
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface AppkeyCheck {
}
