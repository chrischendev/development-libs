package com.chris.framework.builder.model;

import com.chris.framework.builder.annotation.query.Query;
import com.chris.framework.builder.core.exception.BaseClassNotFoundException;
import com.chris.framework.builder.core.manager.QueryManager;
import com.chris.framework.builder.model.object.QueryParams;
import com.chris.framework.builder.utils.ControllerUtils;
import com.chris.framework.builder.utils.ExpandUtils;

import java.util.List;

/**
 * ObjectBuilder
 * com.chris.framework.builder.model
 * Created by Chris Chen
 * 2018/2/13
 * Explain:针对框架一键处理的工具，可以继承
 */
public abstract class MyService implements IService {
    @Override
    public <T> Integer add(T entity) {
        return null;
    }

    @Override
    public <T> Boolean remove(Class<T> clazz, Integer id) {
        return null;
    }

    @Override
    public <T> Boolean update(T entity) {
        return null;
    }

    @Override
    public <T> Boolean update(Class<T> clazz, String fieldName, Object fieldValue) {
        return null;
    }

    @Override
    public <T> T get(Class<T> clazz, Object params) {
        return ControllerUtils.get(clazz, params);
    }

    @Override
    public <T> T get(Class<T> clazz, Integer id) {
        return null;
    }

    @Override
    public <T> T get(Class<T> clazz, String fieldName, Object fieldValue) {
        return null;
    }

    @Override
    public <T> List<T> getList() {
        return null;
    }

    @Override
    public <T> PageModel<T> getPage(PageParams pageParams) {
        return null;
    }

    @Override
    public <T> List<T> queryList(QueryParams queryParams, Class<T> clazz) {
        Class<? extends QueryParams> queryParamsClass = queryParams.getClass();
        Query annotation = queryParamsClass.getAnnotation(Query.class);
        if (annotation == null) {
            throw new BaseClassNotFoundException();
        }
        Class<?> baseClass = annotation.value();
        List ts = QueryManager.queryList(queryParams, baseClass);
        return ExpandUtils.expandList(ts, clazz);
    }

    @Override
    public <T> PageModel<T> queryPage(QueryParams queryParams, Class<T> clazz) {
        Class<? extends QueryParams> queryParamsClass = queryParams.getClass();
        Query annotation = queryParamsClass.getAnnotation(Query.class);
        if (annotation == null) {
            throw new BaseClassNotFoundException();
        }
        Class<?> baseClass = annotation.value();
        return QueryManager.queryPage(queryParams, baseClass).expandPage(clazz);
    }
}
