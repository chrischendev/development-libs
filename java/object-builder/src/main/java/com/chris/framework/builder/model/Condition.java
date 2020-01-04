package com.chris.framework.builder.model;

import com.chris.framework.builder.core.exception.PageParamsNotFoundException;

/**
 * ChrisFrameworkObjectBuilder
 * com.chris.framework.builder.model
 * Created by Chris Chen
 * 2018/1/18
 * Explain:查询条件
 */
public class Condition {
    private Class<?> aClass;
    private String tableName;
    private StringBuilder sql;
    private Sort sort;//这个排序有待进一步处理

    public Condition() {
    }

    public Condition(StringBuilder sql) {
        this.sql = sql;
    }

    public Condition(Class<?> aClass, StringBuilder sql) {
        this.aClass = aClass;
        this.sql = sql;
    }

    public Condition(StringBuilder sql, Sort sort) {
        this.sql = sql;
        this.sort = sort;
    }

    public Condition(Class<?> aClass, StringBuilder sql, Sort sort) {
        this.aClass = aClass;
        this.sql = sql;
        this.sort = sort;
    }

    public Condition(Class<?> aClass, String tableName, StringBuilder sql, Sort sort) {
        this.aClass = aClass;
        this.tableName = tableName;
        this.sql = sql;
        this.sort = sort;
    }

    public String getTableName() {
        return tableName;
    }

    public Condition setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public StringBuilder getSql() {
        return sql;
    }

    public Condition setSql(StringBuilder sql) {
        this.sql = sql;
        return this;
    }

    /**
     * 生成page条件
     *
     * @param page
     * @param pageSize
     * @return
     */
    public Pageable page(int page, int pageSize) {
        Pageable pageable = new Pageable(this, page, pageSize);
        return pageable;
    }

    /**
     * 生成page条件
     *
     * @param pageParams
     * @return
     */
    public Pageable page(PageParams pageParams) {
        if (pageParams == null) {
            throw new PageParamsNotFoundException();
        }
        Pageable pageable = new Pageable(this, pageParams.page, pageParams.pageSize);
        return pageable;
    }

    public Sort getSort() {
        return sort;
    }

    public Condition sort(Sort sort) {
        this.sort = sort;
        return this;
    }

    public Condition sort(String sortType, String byField) {
        this.sort = new Sort(sortType.toUpperCase(), byField);
        return this;
    }

    public Condition sort(String sortType) {
        this.sort = new Sort(sortType, "id");
        return this;
    }

    public Class<?> getaClass() {
        return aClass;
    }

    public Condition setaClass(Class<?> aClass) {
        this.aClass = aClass;
        return this;
    }

    /**
     * 构建sql
     *
     * @return
     */
    public String createSql() {
        StringBuilder cond = new StringBuilder(sql);
        if (sort != null) {
            cond.append(" ORDER BY " + sort.getByField() + " " + sort.getSortType());
        }
        return replace(cond);
    }

    public String orderBy() {
        StringBuilder cond = new StringBuilder(sql);
        if (sort != null) {
            cond.append(" ORDER BY " + sort.getByField() + " " + sort.getSortType());
        }

        return replace(cond);
    }

    private String replace(StringBuilder sb) {
        return replace(sb.toString());
    }

    private String replace(String str) {
        //return str.trim().replace("1=1 AND", "").replace("1=1", "").trim();
        return str.trim();
    }
}
