package com.chris.framework.builder.annotation.query;

/**
 * ChrisFrameworkObjectBuilder
 * com.chris.framework.builder.annotation
 * Created by Chris Chen
 * 2018/1/17
 * Explain:
 */
public enum QueryCompare {
    EQUAL(0, "=", "等于"),
    THAN(1, ">", "大于"),
    NOT_THAN(3, "<=", "不大于"),
    LESS(4, "<", "小于"),
    NOT_LESS(5, ">=", "不小于"),
    BETWEEN(6, " BETWEEN ", "中间"),
    NOT_BETWEEN(6, " NOT BETWEEN ", "不在中间"),
    IN(8, " IN ", "在结果集中"),
    NOT(9, " NOT ", "不等于"),
    NOT_IN(10, " NOT IN ", "不在其中"),
    LIKE(11, " LIKE ", "匹配");

    private int code;
    private String symbol;
    private String explain;

    QueryCompare(int code, String symbol, String explain) {
        this.code = code;
        this.symbol = symbol;
        this.explain = explain;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }
}
