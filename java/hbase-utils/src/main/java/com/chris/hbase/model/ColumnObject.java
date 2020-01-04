package com.chris.hbase.model;

/**
 * Create by Chris Chen
 * Create on 2019-03-12 09:07 星期二
 * This class be use for: 列对象
 */
public class ColumnObject<T> {
    private String columnFamily;
    private String column;
    private T value;

    public ColumnObject() {
    }

    public static ColumnObject get() {
        return new ColumnObject();
    }

    public static <T> ColumnObject create(String columnFamily, String column, T value) {
        return new ColumnObject(columnFamily, column, value);
    }

    public ColumnObject(String columnFamily, String column, T value) {
        this.columnFamily = columnFamily;
        this.column = column;
        this.value = value;
    }

    public String getColumnFamily() {
        return columnFamily;
    }

    public ColumnObject setColumnFamily(String columnFamily) {
        this.columnFamily = columnFamily;
        return this;
    }

    public String getColumn() {
        return column;
    }

    public ColumnObject setColumn(String column) {
        this.column = column;
        return this;
    }

    public T getValue() {
        return value;
    }

    public ColumnObject setValue(T value) {
        this.value = value;
        return this;
    }
}
