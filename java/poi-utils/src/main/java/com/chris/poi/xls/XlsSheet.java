package com.chris.poi.xls;

import java.lang.annotation.*;

/**
 * Created by Chris Chen
 * 2018/09/18
 * Explain: 数据表名
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface XlsSheet {
    String value();

    int maxLines() default 65534;//单表支持的最大数据行数

    int titleRowIndex() default 0;//标题行的位置

    int dataRowStart() default 1;//数据行起始行号
}
