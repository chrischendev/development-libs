package com.chris.es.jest.utils;

import com.chris.es.jest.model.EsSearchParams;
import com.chris.es.jest.model.PageData;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.sort.Sort;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris Chen
 * 2018/10/11
 * Explain: ElasticSearch工具
 * 主要职责是搜索
 */

public class ESUtils {
    /**
     * 最大读取数
     */
    public static Integer PAGE_SIZE_MAX = 10000;

    /**
     * 获取最大读取数
     *
     * @return 最大读取数
     */
    public static Integer getPageSizeMax() {
        return PAGE_SIZE_MAX;
    }

    /**
     * 设置最大读取数
     *
     * @param maxPageSize 最大读取数
     */
    public static void setPageSizeMax(Integer maxPageSize) {
        if (maxPageSize != null && maxPageSize.intValue() > 0) {
            ESUtils.PAGE_SIZE_MAX = maxPageSize;
        }
    }

    /**
     * 把hit集合转换为对象集合
     *
     * @param hitList
     * @param <T>
     * @return
     */
    public static <T> List<T> converFromHitList(List<SearchResult.Hit<T, Void>> hitList) {
        if (hitList == null || hitList.size() == 0) {
            return null;
        }
        List<T> list = new ArrayList<>();
        hitList.stream().forEach(hit -> {
            T source = hit.source;
            setDefaultValue(source, "id", hit.id);
            list.add(source);
        });
        return list;
    }

    /**
     * 把hit集合转换为对象集合 带自定义的id
     *
     * @param hitList
     * @param idFieldname
     * @param <T>
     * @return
     */
    public static <T> List<T> converFromHitList(List<SearchResult.Hit<T, Void>> hitList, String idFieldname) {
        if (hitList == null || hitList.size() == 0) {
            return null;
        }
        List<T> list = new ArrayList<>();
        hitList.stream().forEach(hit -> {
            T source = hit.source;
            setDefaultValue(source, idFieldname, hit.id);
            list.add(source);
        });
        return list;
    }

    /**
     * 从hit集合中获取第一条数据
     *
     * @param hitList
     * @param <T>
     * @return
     */
    public static <T> T converFirstFromHitList(List<SearchResult.Hit<T, Void>> hitList) {
        if (hitList == null || hitList.size() == 0) {
            return null;
        }
        SearchResult.Hit<T, Void> hit = hitList.get(0);
        return hitToEntity(hit);
    }

    /**
     * hit转换为entity
     *
     * @param hit
     * @param <T>
     * @return
     */
    public static <T> T hitToEntity(SearchResult.Hit<T, Void> hit) {
        T source = hit.source;
        setDefaultValue(source, "id", hit.id);
        return source;
    }

    /**
     * 根据条件配到第一条数据
     * 可以自己取id和对象
     * 主要用作更新
     *
     * @param jestClient
     * @param params
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> SearchResult.Hit<T, ?> findOne(JestClient jestClient, EsSearchParams params, Class<T> clazz) {
        SearchResult searchResult = searchResult(jestClient, params);
        if (searchResult != null && searchResult.isSucceeded()) {
            List<SearchResult.Hit<T, Void>> hits = searchResult.getHits(clazz);
            if (hits != null && hits.size() > 0) {
                SearchResult.Hit<T, Void> hit = hits.get(0);
                return hit;
            }
        }
        return null;
    }

    //搜索按照最大限制允许的数据集合 带id
    public static <T> List<T> searchListWithId(JestClient jestClient, EsSearchParams params, Class<T> clazz, String idFieldName) {
        List<T> dataList = new ArrayList<>();
        SearchResult result = searchResult(jestClient, params);
        if (result == null || !result.isSucceeded()) {
            return dataList;
        }
        List<SearchResult.Hit<T, Void>> hits = result.getHits(clazz);
        dataList = ESUtils.converFromHitList(hits, idFieldName);
        return dataList;
    }

    //搜索按照最大限制允许的数据集合
    public static <T> List<T> searchList(JestClient jestClient, EsSearchParams params, Class<T> clazz) {
        List<T> dataList = new ArrayList<>();
        SearchResult result = searchResult(jestClient, params);
        if (result == null || !result.isSucceeded()) {
            return dataList;
        }
        List<SearchResult.Hit<T, Void>> hits = result.getHits(clazz);
        dataList = ESUtils.converFromHitList(hits);

//        result=null;
//        System.gc();

        return dataList;
    }

    /**
     * ElasticSearch搜索
     *
     * @return
     * @throws IOException
     */
    public static <T> PageData<T> searchPage(JestClient jestClient, EsSearchParams params, Class<T> clazz) {
        SearchResult result = searchResult(jestClient, params);
        if (result == null || !result.isSucceeded()) {
            return PageData.buildNull();
        }
        List<SearchResult.Hit<T, Void>> hits = result.getHits(clazz);
        List<T> dataList = ESUtils.converFromHitList(hits);
        if (dataList == null) {
            return PageData.buildNull();
        }
        int page = params.getPage();
        int pageSize = params.getPageSize();
        long total = 0;
        try {
            total = result.getTotal();
        } catch (NoSuchMethodError e) {
            throw new RuntimeException("有问题");
        }

//        result=null;
//        System.gc();

        return PageData.get(clazz)
                .setPage(page)
                .setPageSize(pageSize)
                .setTotal(total)
                .setHasNext((page + 1) * pageSize < total)
                .setDataList(dataList);
    }

    public static SearchResult searchResult(JestClient jestClient, EsSearchParams params) {
        SearchSourceBuilder ssb = new SearchSourceBuilder();
        BoolQueryBuilder bqb = QueryBuilders.boolQuery();
        List<QueryBuilder> queryBuilderList = new ArrayList<>();//搜集查询条件
        WildcardQueryBuilder mustWildcardQueryBuilder = null;
        MultiMatchQueryBuilder multiMatchQueryBuilder = null;
        //设置查询结果字段
        String[] sourceFields = params.getSourceFields();
        if (sourceFields != null && sourceFields.length > 0) {
            ssb.fetchSource(sourceFields, null);
        }
        //构建多条件查询
        ////1-1.一个字段对应一个关键字
        EsSearchHelper.processKV(params, bqb, queryBuilderList);
        ////1-2.一个字段对应多个关键字
        EsSearchHelper.processFieldMultiValue(params, bqb, queryBuilderList);
        ////2-1.单字段匹配模糊查询 should
        EsSearchHelper.processShouldWildcardField(params, bqb, queryBuilderList);
        ////2-2.单字段匹配模糊查询 must
        EsSearchHelper.processMustWildcardField(params, bqb, mustWildcardQueryBuilder, queryBuilderList);
        ////3.多个个字段对应一个关键字 精确匹配
        EsSearchHelper.processMultiField(params, bqb, multiMatchQueryBuilder, queryBuilderList);
        ////3-2.多个个字段对应一个关键字 模糊匹配
        EsSearchHelper.processMultiWildcardField(params, bqb, multiMatchQueryBuilder, mustWildcardQueryBuilder, queryBuilderList);
        ////4. 值区间查询
        EsSearchHelper.processRangeField(params, bqb, queryBuilderList);
        ////5. 极值聚合查询
        //EsSearchHelper.processExtremeLongValueField(jestClient, params, bqb, queryBuilderList);

        int page = params.getPage();
        int pageSize = params.getPageSize();
        int from = page * pageSize;
        ssb.query(bqb).from(from).size(pageSize);//分页搜索

        String query = ssb.toString();
        if (params.isShowQueryString()) {
            System.out.println(query);
        }
        Search.Builder builder = new Search.Builder(query)
                .addIndex(params.getIndex())
                .addType(params.getType());
        String sortFieldName = params.getSortFieldName();
        if (!EsSearchHelper.checkIsEmpty(sortFieldName)) {
            builder.addSort(new Sort(sortFieldName, params.getSortMode()));
        }
        Search search = builder.build();
        try {
            return jestClient.execute(search);
        } catch (IOException e) {
            //logger.d("ES读取异常");
            //e.printStackTrace();
        }
        return null;
    }

    // 字符串参数集合转为数组
    public static String listToArray(List<String> batteryNameList) {
        if (batteryNameList == null || batteryNameList.size() == 0) {
            return null;
        }
        int size = batteryNameList.size();
        String[] batteryNames = new String[size];
        for (int i = 0; i < size; i++) {
            batteryNames[i] = batteryNameList.get(i);
        }
        return null;
    }

    /**
     * 查找特定字段，设置默认值
     *
     * @param object
     * @param value
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static void setDefaultValue(Object object, String fieldName, Object value) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            if (field != null) {
                field.setAccessible(true);
                field.set(object, value);//设置默认值
                field.setAccessible(false);
            }
        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
        } catch (IllegalAccessException e) {
//            e.printStackTrace();
        }

    }
}
