package com.chris.poi.xls;

import java.lang.annotation.*;

/**
 * Created by Chris Chen
 * 2018/09/18
 * Explain: 数据表头 排除标记 有此记号不会在数据表中写入
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface XlsColumnExclude {
}
