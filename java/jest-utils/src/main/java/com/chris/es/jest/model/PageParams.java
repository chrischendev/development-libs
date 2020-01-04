package com.chris.es.jest.model;

/**
 * Created by Chris Chen
 * 2018/10/11
 * Explain: 分页请求参数
 */

public class PageParams {
    public int page;
    public int pageSize;

    public PageParams() {
    }

    public PageParams(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public PageParams(int page, Integer pageSize) {
        this.page = page;
        this.pageSize = pageSize;
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
}
