package com.chris.hbase.model;

import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.of;

/**
 * Create by Chris Chen
 * Create on 2019-03-12 09:07 星期二
 * This class be use for: 列对象
 */
public class RowObject<T> {
    private String rowKey;
    private List<ColumnObject<T>> columnObjectList;

    public RowObject() {
        this.columnObjectList = new ArrayList<>();
    }

    public static RowObject get() {
        return new RowObject();
    }

    public static <T> RowObject create(String rowKey, List<ColumnObject<T>> columnObjectList) {
        return new RowObject(rowKey, columnObjectList);
    }

    public RowObject(String rowKey, List<ColumnObject<T>> columnObjectList) {
        this.rowKey = rowKey;
        this.columnObjectList = columnObjectList;
    }

    public String getRowKey() {
        return rowKey;
    }

    public RowObject setRowKey(String rowKey) {
        this.rowKey = rowKey;
        return this;
    }

    public List<ColumnObject<T>> getColumnObjectList() {
        return columnObjectList;
    }

    public void setColumnObjectList(List<ColumnObject<T>> columnObjectList) {
        this.columnObjectList = columnObjectList;
    }

    public RowObject addColumnObject(ColumnObject<T> columnObject) {
        of(this.columnObjectList).ifPresent(columnObjectList -> columnObjectList.add(columnObject));
        return this;
    }

}
