package com.chris.framework.builder.model;

/**
 * ObjectBuilder
 * com.chris.framework.builder.model
 * Created by Chris Chen
 * 2018/2/24
 * Explain:根据数据库表映射生成的实体类信息
 */
public class Orm {
    private String className;//类名
    private String tableName;//关联的表名

    public Orm(String className, String tableName) {
        this.className = className;
        this.tableName = tableName;
    }
}
