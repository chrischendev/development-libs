package com.chris.poi.xls;

/**
 * Created by Chris Chen
 * 2018/10/08
 * Explain: 单元格校验错误信息
 */

public class XlsErrorCell {
    private int rowIndex;//行号
    private int columnIndex;//列号
    private transient int errorCode = 0;//错误编号 0 表示未知或者未定义
    private String errorInfo;//错误信息描述

    private XlsErrorCell() {
    }

    public static XlsErrorCell get() {
        return new XlsErrorCell();
    }

    public XlsErrorCell(int rowIndex, int columnIndex, int errorCode, String errorInfo) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.errorCode = errorCode;
        this.errorInfo = errorInfo;
    }

    public XlsErrorCell set(int rowIndex, int columnIndex, int errorCode, String errorInfo) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.errorCode = errorCode;
        this.errorInfo = errorInfo;
        return this;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public XlsErrorCell setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
        return this;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public XlsErrorCell setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
        return this;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public XlsErrorCell setErrorCode(int errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public XlsErrorCell setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
        return this;
    }
}
