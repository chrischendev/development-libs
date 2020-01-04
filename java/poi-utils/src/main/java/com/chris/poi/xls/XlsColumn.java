package com.chris.poi.xls;

import java.lang.annotation.*;

/**
 * Created by Chris Chen
 * 2018/09/18
 * Explain: 数据表头列名
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface XlsColumn {
    /**
     * 映射工作表列名
     *
     * @return 工作表列名
     */
    String value();

    /**
     * 列索引号
     * 暂时没有设计通过此属性去控制列的顺序的功能
     *
     * @return 列索引号
     */
    int columnIndex() default -1;

    /**
     * 列宽
     * 默认-1,框架将按照标题行的字符长度来适配
     *
     * @return 列宽
     */
    int width() default -1;

    /**
     * 是否必填
     * 读取时会做判断
     *
     * @return 是否必填
     */
    boolean required() default false;

    /**
     * 是否排除
     * true表示在读写时不包括这个字段
     *
     * @return 是否排除
     */
    boolean exclude() default false;
}
