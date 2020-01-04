package com.chris.framework.builder.model;

import com.chris.framework.builder.model.object.QueryParams;

import java.util.List;

/**
 * ObjectBuilder
 * com.chris.framework.builder.model
 * Created by Chris Chen
 * 2018/2/13
 * Explain:规范工具service
 */
public interface IService {
    <T> Integer add(T entity);//增加

    <T> Boolean remove(Class<T> clazz, Integer id);//删除

    <T> Boolean update(T entity);//修改

    <T> Boolean update(Class<T> clazz, String fieldName, Object fieldValue);//修改一个字段

    <T> T get(Class<T> clazz, Object params); //获取一个对象，将参数放入params，只允许有一个参数

    <T> T get(Class<T> clazz, Integer id); //获取一个对象

    <T> T get(Class<T> clazz, String fieldName, Object fieldValue); //获取一个对象

    <T> List<T> getList();//获取集合

    <T> PageModel<T> getPage(PageParams pageParams);//获取分页

    <T> List<T> queryList(QueryParams queryParams, Class<T> clazz);//多条件查询集合

    <T> PageModel<T> queryPage(QueryParams queryParams, Class<T> clazz);//多条件查询分页

}
