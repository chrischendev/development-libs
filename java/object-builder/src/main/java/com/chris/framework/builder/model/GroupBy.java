package com.chris.framework.builder.model;

/**
 * ChrisFrameworkObjectBuilder
 * com.chris.framework.builder.model
 * Created by Chris Chen
 * 2018/1/18
 * Explain:分组
 */
public class GroupBy {
    private String field;

    public GroupBy() {
    }

    public GroupBy(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
