package com.chris.framework.builder.libs.springboot.controller;

import com.chris.framework.builder.core.manager.QueryManager;
import com.chris.framework.builder.libs.springboot.service.BaseService;
import com.chris.framework.builder.model.PageModel;
import com.chris.framework.builder.model.PageParams;
import com.chris.framework.builder.model.object.QueryParams;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * ydx-web-demo
 * com.ydx.demo.libs
 * Created by Chris Chen
 * 2018/2/10
 * Explain:基本RequestBody+ResponseBody风格的Controller
 */
@RestController
public abstract class BaseBodyFulController<T, S extends BaseService> {
    protected S service;

    protected abstract S service(); //设置service

    @PostConstruct
    public void init() {
        this.service = service();
    }

    @RequestMapping("/add")//增加
    public Integer add(@RequestBody T entity) {
        return service.addOne(entity);
    }

    @RequestMapping("/remove")//删除
    public Boolean remove(Integer id) {
        return service.removeOne(id);
    }

    @RequestMapping("/update")//修改
    public Boolean update(@RequestBody T entity) {
        return service.updateEntity(entity);
    }

    @RequestMapping("/get")//获取
    public T get(Integer id) {
        return (T) service.getEntity(id);
    }

    @RequestMapping("/getList")//获取集合
    public List<T> getList() {
        return service.getAll();
    }

    @RequestMapping("/getPage")//获取分页
    public PageModel<T> getPage(@RequestBody PageParams pageParams) {
        return service.getPage(pageParams);
    }

    @RequestMapping("/queryList")//查询集合
    public List<T> queryList(@RequestBody QueryParams queryParams) {
        return QueryManager.queryList(queryParams, (Class<T>) Object.class);
    }

    @RequestMapping("/queryPage")//查询分页
    public PageModel<T> queryPage(@RequestBody QueryParams queryParams) {
        return QueryManager.queryPage(queryParams, (Class<T>) Object.class);
    }

}
