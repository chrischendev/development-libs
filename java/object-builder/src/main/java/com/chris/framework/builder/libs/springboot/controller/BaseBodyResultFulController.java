package com.chris.framework.builder.libs.springboot.controller;

import com.chris.framework.builder.libs.springboot.service.BaseService;
import com.chris.framework.builder.model.IdParams;
import com.chris.framework.builder.model.PageModel;
import com.chris.framework.builder.model.PageParams;
import com.chris.framework.builder.net.protocol.NetRequest;
import com.chris.framework.builder.net.protocol.NetResult;
import com.chris.framework.builder.utils.ResultUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
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
public abstract class BaseBodyResultFulController<T, S extends BaseService> {
    public S service;

    public abstract S service(); //设置service

    @PostConstruct
    public void init() {
        this.service = service();
    }

    @ApiOperation(value = "添加", notes = "", httpMethod = "POST")
    @PostMapping("/add")//增加
    public NetResult<Integer> add(@RequestBody NetRequest<T> request) {
        return ResultUtils.buildResult(service.addOne(request.getParams()));
    }

    @ApiOperation(value = "删除", notes = "请提供ID", httpMethod = "POST")
    @PostMapping("/remove")//删除
    public NetResult<Boolean> remove(@RequestBody NetRequest<IdParams> request) {
        return ResultUtils.buildResult(service.removeOne(request.getParams().getId()));
    }

    @ApiOperation(value = "修改", notes = "", httpMethod = "POST")
    @PostMapping("/update")//修改
    public NetResult<Boolean> update(@RequestBody NetRequest<T> request) {
        return ResultUtils.buildResult(service.updateEntity(request.getParams()));
    }

    @ApiOperation(value = "获取", notes = "请提供ID", httpMethod = "POST")
    @PostMapping("/get")//获取
    public NetResult<T> get(@RequestBody NetRequest<IdParams> request) {
        return ResultUtils.buildResult((T) service.getEntity(request.getParams().getId()));
    }

    @ApiOperation(value = "获取列表", notes = "", httpMethod = "POST")
    @PostMapping("/getList")//获取集合
    public NetResult<List<T>> getList(@RequestBody NetRequest request) {
        return ResultUtils.buildResult(service.getAll());
    }

    @ApiOperation(value = "获取分页", notes = "请提供分页参数", httpMethod = "POST")
    @PostMapping("/getPage")//获取分页
    public NetResult<PageModel<T>> getPage(@RequestBody NetRequest<PageParams> request) {
        return ResultUtils.buildResult(service.getPage(request.getParams()));
    }
}
