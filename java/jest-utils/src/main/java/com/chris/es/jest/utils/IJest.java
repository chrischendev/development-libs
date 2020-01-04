package com.chris.es.jest.utils;

import java.util.List;

/**
 * Created by Chris Chen
 * 2018/11/22
 * Explain:
 */

public interface IJest {
    <T> void save(T entity, String index, String type);

    <T> void update(T entity, String index, String type, String id);

    <T> void saveAll(List<T> entitys, String index, String type);

    <T> List<T> findAll(Class<T> clazz, String index, String type);
}
