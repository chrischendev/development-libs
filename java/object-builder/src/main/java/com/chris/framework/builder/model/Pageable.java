package com.chris.framework.builder.model;

/**
 * ChrisFrameworkObjectBuilder
 * com.chris.framework.builder.model
 * Created by Chris Chen
 * 2018/1/18
 * Explain:可分页参数
 */
public class Pageable {
    private Condition condition;
    private int page;
    private int pageSize;

    public Pageable() {
    }

    public Pageable(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public Pageable(Condition condition, int page, int pageSize) {
        this.condition = condition;
        this.page = page;
        this.pageSize = pageSize;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 最终构建
     *
     * @return
     */
    public String create() {
        StringBuilder pageStr = new StringBuilder(condition.createSql());
        pageStr.append(" LIMIT " + ((page - 1) * pageSize) + "," + pageSize);
        return pageStr.toString();
    }
}
