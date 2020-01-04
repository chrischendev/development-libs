package com.chris.framework.builder.annotation.query.compare;

/**
 * ChrisFrameworkObjectBuilder
 * com.chris.framework.builder.annotation.query.compare
 * Created by Chris Chen
 * 2018/2/6
 * Explain:
 */
public enum OrderType {
    DESC(0," DESC ","降序"),
    ASC(1," ASC ","升序");

    private int code;
    private String symbol;
    private String explain;

    OrderType(int code, String symbol, String explain) {
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
