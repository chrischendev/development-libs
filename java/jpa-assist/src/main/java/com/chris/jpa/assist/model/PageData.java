package com.chris.jpa.assist.model;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Explain:分页数据模型
 */
public class PageData<T> {
    public int page;//当前页码
    public int pageSize;//页面大小
    public long count;//数据总数
    public boolean hasNext;//是否还有下一页
    public List<T> dataList;//数据列表

    public PageData() {
        this.dataList = new ArrayList<>();
    }

    public PageData(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
        this.dataList = new ArrayList<>();
    }

    public PageData(int page, int pageSize, long count) {
        this.page = page;
        this.pageSize = pageSize;
        this.count = count;
        this.dataList = new ArrayList<>();
    }

    public PageData(int page, int pageSize, List<T> dataList) {
        this.page = page;
        this.pageSize = pageSize;
        this.dataList = dataList;
    }

    public PageData(int page, int pageSize, long count, List<T> dataList) {
        this.page = page;
        this.pageSize = pageSize;
        this.count = count;
        this.dataList = dataList;
    }

    public PageData(int page, int pageSize, long count, boolean hasNext, List<T> dataList) {
        this.page = page;
        this.pageSize = pageSize;
        this.count = count;
        this.hasNext = hasNext;
        this.dataList = dataList;
    }

    /**
     * 把Page对象转换为PageModel对象
     *
     * @param pageObj
     * @param <T>
     * @return
     */
    public static <T> PageData<T> fromPage(Page pageObj) {
        PageData<T> pageData = new PageData<>();
        pageData.page = pageObj.getNumber();
        pageData.pageSize = pageObj.getSize();
        pageData.count = pageObj.getTotalElements();
        pageData.hasNext = pageObj.hasNext();
        pageData.dataList = pageObj.getContent();
        return pageData;
    }

    /**
     * 数据格式转换
     *
     * @param converter
     * @param <T1>
     * @return
     */
    public <T1> PageData<T1> map(Converter<T, T1> converter) {
        PageData<T1> pageData = new PageData<>();
        pageData.page = this.page;
        pageData.pageSize = this.pageSize;
        pageData.count = this.count;
        pageData.hasNext = this.hasNext;
        pageData.dataList = this.dataList.stream().map(data -> converter.convert(data)).collect(Collectors.toList());

        return pageData;
    }
}
