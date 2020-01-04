package com.chris.framework.builder.model;

import com.chris.framework.builder.utils.EntityUtils;
import com.chris.framework.builder.utils.ExpandUtils;

import java.util.List;

/**
 * MeiyueJavaProject
 * com.meiyue.bean.base
 * Created by Chris Chen
 * 2017/9/14
 * Explain:分页数据模型
 */
public class PageModel<DataType> {
    public int page;//当前页码
    public int pageSize;//页面大小
    public long count;//数据总数
    public boolean hasNext;//是否还有下一页
    public List<DataType> dataList;//数据列表

    public PageModel() {
    }

    public PageModel(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public PageModel(int page, int pageSize, long count) {
        this.page = page;
        this.pageSize = pageSize;
        this.count = count;
    }

    public PageModel(int page, int pageSize, List<DataType> dataList) {
        this.page = page;
        this.pageSize = pageSize;
        this.dataList = dataList;
    }

    public PageModel(int page, int pageSize, long count, boolean hasNext, List<DataType> dataList) {
        this.page = page;
        this.pageSize = pageSize;
        this.count = count;
        this.hasNext = hasNext;
        this.dataList = dataList;
    }

    /**
     * 专用于将dataList进行扩展的功能方法
     *
     * @param targetObjectClass
     * @param <T>
     * @return
     */
    public <T> PageModel<T> expandPage(Class<T> targetObjectClass) {
        //1. 复制对象
        PageModel<T> pageModel = new PageModel<T>();
        EntityUtils.copyData(this, pageModel);
        //2. 将dataList扩展后加入
        List<T> expandList = ExpandUtils.expandList(this.dataList, targetObjectClass);
        pageModel.dataList = expandList;
        return pageModel;
    }
}
