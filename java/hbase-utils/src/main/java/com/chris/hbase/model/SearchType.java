package com.chris.hbase.model;

/**
 * Create by Chris Chan
 * Create on 2019/3/26 13:54
 * Use for: 搜索类型
 */
public enum SearchType {
    MATCHS,//精确匹配
    FUZZY,//模糊匹配
    RANGE,//值范围
    IGNORE//忽略 匹配任意字符


}
