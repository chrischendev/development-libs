package com.chris.framework.builder.model;

/**
 * ydx-web-demo
 * com.ydx.demo.model.dba
 * Created by Chris Chen
 * 2018/2/11
 * Explain:数据表列
 */
public class Column {
    private int columnId;//顺序
    private String columnName;//列名
    private String columnType;//列数据类型（数据库内类型）
    private String columnClassName;//列数据类型名
    private Integer columnDisplaySize;//列数据长度
    private Integer isNullable;//是否可以为空
    private Boolean isAutoIncrement;//是否自增长
    private Boolean isSearchable;//是否可以在查询语句中出现
    private Boolean isCaseSensitive;//是否支持区分大小写
    private String pkTableName;//外键关联表名
    private String pkColumnName;//外键关联列名
    private boolean isPrimaryKey;//是否主键

    public Column() {
    }

    public Column(String columnName, String columnClassName) {
        this.columnName = columnName;
        this.columnClassName = columnClassName;
    }

    public int getColumnId() {
        return columnId;
    }

    public void setColumnId(int columnId) {
        this.columnId = columnId;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnClassName() {
        return columnClassName;
    }

    public void setColumnClassName(String columnClassName) {
        this.columnClassName = columnClassName;
    }

    public Integer getColumnDisplaySize() {
        return columnDisplaySize;
    }

    public void setColumnDisplaySize(Integer columnDisplaySize) {
        this.columnDisplaySize = columnDisplaySize;
    }

    public Integer getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(Integer isNullable) {
        this.isNullable = isNullable;
    }

    public Boolean getAutoIncrement() {
        return isAutoIncrement;
    }

    public void setAutoIncrement(Boolean autoIncrement) {
        isAutoIncrement = autoIncrement;
    }

    public Boolean getSearchable() {
        return isSearchable;
    }

    public void setSearchable(Boolean searchable) {
        isSearchable = searchable;
    }

    public Boolean getCaseSensitive() {
        return isCaseSensitive;
    }

    public void setCaseSensitive(Boolean caseSensitive) {
        isCaseSensitive = caseSensitive;
    }

    public String getPkTableName() {
        return pkTableName;
    }

    public void setPkTableName(String pkTableName) {
        this.pkTableName = pkTableName;
    }

    public String getPkColumnName() {
        return pkColumnName;
    }

    public void setPkColumnName(String pkColumnName) {
        this.pkColumnName = pkColumnName;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }
}
