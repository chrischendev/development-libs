package com.chris.es.jest.model;


import com.google.common.base.Converter;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris Chen
 * 2018/10/11
 * Explain: 分页数据
 */

public class PageData<T> {
    private int page;
    private int pageSize;
    private long total;
    private boolean hasNext;

    private List<T> dataList;

    public PageData() {
    }

    public PageData(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public PageData(int page, int pageSize, long total, boolean hasNext) {
        this.page = page;
        this.pageSize = pageSize;
        this.total = total;
        this.hasNext = hasNext;
    }

    public PageData(int page, int pageSize, long total, boolean hasNext, List<T> dataList) {
        this.page = page;
        this.pageSize = pageSize;
        this.total = total;
        this.hasNext = hasNext;
        this.dataList = dataList;
    }

    public static <T> PageData<T> get(Class<T> clazz) {
        return new PageData();
    }

    public PageData set(int page, int pageSize, long count, boolean hasNext, List<T> dataList) {
        this.page = page;
        this.pageSize = pageSize;
        this.total = count;
        this.hasNext = hasNext;
        this.dataList = dataList;
        return this;
    }

    public int getPage() {
        return page;
    }

    public PageData setPage(int page) {
        this.page = page;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public PageData setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public long getTotal() {
        return total;
    }

    public PageData setTotal(long total) {
        this.total = total;
        return this;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public PageData setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
        return this;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public PageData setDataList(List<T> dataList) {
        this.dataList = dataList;
        return this;
    }

    //格式转换
    public <T1> PageData<T1> convert(Converter<T, T1> converter) {
        PageData<T1> pageData = new PageData<>();
        BeanUtils.copyProperties(this, pageData);
        List<T1> dataList = new ArrayList<>();
        if (this.dataList == null) {
            return buildNull();
        }
        this.dataList.stream().forEach(data -> dataList.add(converter.convert(data)));
        pageData.setDataList(dataList);
        return pageData;
    }

    public static PageData buildNull() {
        return new PageData(0, 0, 0, false, null);
    }

    public static PageData buildEmpty() {
        return new PageData(0, 0, 0, false, new ArrayList());
    }
}
