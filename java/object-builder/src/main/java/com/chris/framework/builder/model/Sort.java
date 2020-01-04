package com.chris.framework.builder.model;

/**
 * ChrisFrameworkObjectBuilder
 * com.chris.framework.builder.model
 * Created by Chris Chen
 * 2018/1/18
 * Explain:排序参数
 */
public class Sort {
    public static final String DESC = "DESC";//降序
    public static final String ASC = "ASC";//升序

    private String sortType = DESC;
    private String byField;

    public Sort() {
    }

    public Sort(String sortType) {
        this.sortType = sortType.toUpperCase();
        this.byField = "id";
    }

    public Sort(String sortType, String byField) {
        this.sortType = sortType.toUpperCase();
        this.byField = byField;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType.toUpperCase();
    }

    public String getByField() {
        return byField;
    }

    public void setByField(String byField) {
        this.byField = byField;
    }
}
