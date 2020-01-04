package com.chris.hbase.model;

import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.of;

/**
 * Create by Chris Chen
 * Create on 2019-03-12 09:07 星期二
 * This class be use for: 列对象
 */
public class TableObject<T> {
    private String tableName;
    private List<RowObject<T>> rowObjectList;

    public TableObject() {
        this.rowObjectList = new ArrayList<>();
    }

    public static TableObject get() {
        return new TableObject();
    }

    public static <T> TableObject create(String tableName, List<RowObject<T>> rowObjectList) {
        return new TableObject(tableName, rowObjectList);
    }

    public TableObject(String tableName, List<RowObject<T>> rowObjectList) {
        this.tableName = tableName;
        this.rowObjectList = rowObjectList;
    }

    public String getTableName() {
        return tableName;
    }

    public TableObject setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public List<RowObject<T>> getRowObjectList() {
        return rowObjectList;
    }

    public TableObject setRowObjectList(List<RowObject<T>> rowObjectList) {
        this.rowObjectList = rowObjectList;
        return this;
    }

    public TableObject addRowbject(RowObject<T> rowObject) {
        of(this.rowObjectList).ifPresent(columnObjectList -> columnObjectList.add(rowObject));
        return this;
    }

}
