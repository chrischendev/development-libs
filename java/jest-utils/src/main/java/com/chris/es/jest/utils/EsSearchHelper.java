package com.chris.es.jest.utils;

import com.chris.es.jest.model.EsSearchParams;
import com.chris.es.jest.model.Range;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.MaxAggregation;
import io.searchbox.core.search.aggregation.MinAggregation;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.max.MaxAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.min.MinAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Chris Chen
 * 2019/02/28
 * Explain: 辅助工具
 */

public class EsSearchHelper {
    public static void processKV(EsSearchParams params, BoolQueryBuilder boolQueryBuilder, List<QueryBuilder> queryBuilderList) {
        ////1-1.一个字段对应一个关键字
        Map<String, Object> fieldMap = params.getFieldMap();
        TermQueryBuilder termQueryBuilder = null;
        if (fieldMap != null && fieldMap.size() > 0) {
            for (Map.Entry<String, Object> entry : fieldMap.entrySet()) {
                Object value = entry.getValue();
                if (checkIsEmpty(value)) {
                    continue;
                }
                termQueryBuilder = QueryBuilders.termQuery(entry.getKey(), value);
                boolQueryBuilder.must(termQueryBuilder);
                queryBuilderList.add(termQueryBuilder);
            }
        }
    }

    public static void processFieldMultiValue(EsSearchParams params, BoolQueryBuilder boolQueryBuilder, List<QueryBuilder> queryBuilderList) {
        ////1-2.一个字段对应多个关键字
        Map<String, Object[]> fieldMultiValueMap = params.getFieldMultiValueMap();
        TermsQueryBuilder termsQueryBuilder1 = null;
        if (fieldMultiValueMap != null && fieldMultiValueMap.size() > 0) {
            for (Map.Entry<String, Object[]> entry : fieldMultiValueMap.entrySet()) {
                Object[] value = entry.getValue();
                if (checkIsEmpty(value)) {
                    continue;
                }
                termsQueryBuilder1 = QueryBuilders.termsQuery(entry.getKey(), value);
                boolQueryBuilder.must(termsQueryBuilder1);
                queryBuilderList.add(termsQueryBuilder1);
            }
        }
    }

    public static void processShouldWildcardField(EsSearchParams params, BoolQueryBuilder boolQueryBuilder, List<QueryBuilder> queryBuilderList) {
        ////2-1.单字段匹配模糊查询 should
        Map<String, String> shouldWildcardFieldMap = params.getShouldWildcardFieldMap();
        WildcardQueryBuilder shouldWildcardQueryBuilder = null;
        if (shouldWildcardFieldMap != null && shouldWildcardFieldMap.size() > 0) {
            for (Map.Entry<String, String> entry : shouldWildcardFieldMap.entrySet()) {
                String keyWords = entry.getValue();//模糊查询关键字 需要添加通配符
                if (checkIsEmpty(keyWords)) {
                    continue;
                }
                shouldWildcardQueryBuilder = QueryBuilders.wildcardQuery(entry.getKey(), keyWords);
                boolQueryBuilder.should(shouldWildcardQueryBuilder);
                queryBuilderList.add(shouldWildcardQueryBuilder);
            }
        }
    }

    public static void processMustWildcardField(EsSearchParams params, BoolQueryBuilder boolQueryBuilder, WildcardQueryBuilder mustWildcardQueryBuilder, List<QueryBuilder> queryBuilderList) {
        ////2-2.单字段匹配模糊查询 must
        Map<String, String> mustWildcardFieldMap = params.getMustWildcardFieldMap();
        if (mustWildcardFieldMap != null && mustWildcardFieldMap.size() > 0) {
            for (Map.Entry<String, String> entry : mustWildcardFieldMap.entrySet()) {
                String keyWords = entry.getValue();//模糊查询关键字 需要添加通配符
                if (checkIsEmpty(keyWords)) {
                    continue;
                }
                mustWildcardQueryBuilder = QueryBuilders.wildcardQuery(entry.getKey(), keyWords);
                boolQueryBuilder.must(mustWildcardQueryBuilder);
                queryBuilderList.add(mustWildcardQueryBuilder);
            }
        }
    }

    public static void processMultiField(EsSearchParams params, BoolQueryBuilder boolQueryBuilder, MultiMatchQueryBuilder multiMatchQueryBuilder, List<QueryBuilder> queryBuilderList) {
        ////3.多个个字段对应一个关键字 精确匹配
        Map<String, String[]> multiFieldMap = params.getMultiFieldMap();
        if (multiFieldMap != null && multiFieldMap.size() > 0) {
            for (Map.Entry<String, String[]> entry : multiFieldMap.entrySet()) {
                String key = entry.getKey();
                String[] value = entry.getValue();
                if (org.springframework.util.StringUtils.isEmpty(key) || StringUtils.isEmpty(key.trim()) || value == null || value.length == 0) {
                    continue;
                }
                multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(key, value);
                boolQueryBuilder.must(multiMatchQueryBuilder);
                queryBuilderList.add(multiMatchQueryBuilder);
            }
        }
    }

    public static void processMultiWildcardField(EsSearchParams params, BoolQueryBuilder boolQueryBuilder, MultiMatchQueryBuilder multiMatchQueryBuilder, WildcardQueryBuilder mustWildcardQueryBuilder, List<QueryBuilder> queryBuilderList) {
        ////3-2.多个个字段对应一个关键字 模糊匹配
        Map<String, String[]> multiWildcardFieldMap = params.getMultiWildcardFieldMap();
        if (multiWildcardFieldMap != null && multiWildcardFieldMap.size() > 0) {
            for (Map.Entry<String, String[]> entry : multiWildcardFieldMap.entrySet()) {
                ////一个元素一个bool查询
                BoolQueryBuilder mulBqb = QueryBuilders.boolQuery();
                ////取得共同的值
                String valWord = entry.getKey();
                ////取得所有的字段
                String[] fields = entry.getValue();
                ////遍历字段
                for (String field : fields) {
                    if (checkIsEmpty(valWord)) {
                        continue;
                    }
                    mustWildcardQueryBuilder = QueryBuilders.wildcardQuery(field, valWord);
                    mulBqb.should(mustWildcardQueryBuilder);
                }
                boolQueryBuilder.must(mulBqb);
                queryBuilderList.add(multiMatchQueryBuilder);
            }
        }
    }

    public static void processRangeField(EsSearchParams params, BoolQueryBuilder boolQueryBuilder, List<QueryBuilder> queryBuilderList) {
        ////4. 值区间查询 全闭
        Map<String, Range<?>> rangeFieldMap = params.getRangeFieldMap();
        RangeQueryBuilder rangeQueryBuilder = null;
        if (rangeFieldMap != null && rangeFieldMap.size() > 0) {
            for (Map.Entry<String, Range<?>> entry : rangeFieldMap.entrySet()) {
                String key = entry.getKey();
                Range<?> range = entry.getValue();
                if (range == null) {
                    continue;
                }
                rangeQueryBuilder = QueryBuilders.rangeQuery(key)
                        .gte(range.getMin())
                        .lte(range.getMax());
                boolQueryBuilder.must(rangeQueryBuilder);
                queryBuilderList.add(rangeQueryBuilder);
            }
        }
        //////4-2. 值区间查询 左开右闭
        Map<String, Range<?>> rangeGtLteFieldMap = params.getRangeGtLteFieldMap();
        RangeQueryBuilder rangeQueryBuilder2 = null;
        if (rangeGtLteFieldMap != null && rangeGtLteFieldMap.size() > 0) {
            for (Map.Entry<String, Range<?>> entry : rangeGtLteFieldMap.entrySet()) {
                String key = entry.getKey();
                Range<?> range = entry.getValue();
                if (range == null) {
                    continue;
                }
                rangeQueryBuilder2 = QueryBuilders.rangeQuery(key)
                        .gt(range.getMin())
                        .lte(range.getMax());
                boolQueryBuilder.must(rangeQueryBuilder2);
                queryBuilderList.add(rangeQueryBuilder2);
            }
        }
        //////4-3. 值区间查询 左闭右开
        Map<String, Range<?>> rangeGteLtFieldMap = params.getRangeGteLtFieldMap();
        RangeQueryBuilder rangeQueryBuilder3 = null;
        if (rangeGteLtFieldMap != null && rangeGteLtFieldMap.size() > 0) {
            for (Map.Entry<String, Range<?>> entry : rangeGteLtFieldMap.entrySet()) {
                String key = entry.getKey();
                Range<?> range = entry.getValue();
                if (range == null) {
                    continue;
                }
                rangeQueryBuilder3 = QueryBuilders.rangeQuery(key)
                        .gte(range.getMin())
                        .lt(range.getMax());
                boolQueryBuilder.must(rangeQueryBuilder2);
                queryBuilderList.add(rangeQueryBuilder3);
            }
        }
        //////4-4. 值区间查询 全开
        Map<String, Range<?>> rangeGtLtFieldMap = params.getRangeGtLtFieldMap();
        RangeQueryBuilder rangeQueryBuilder4 = null;
        if (rangeGtLtFieldMap != null && rangeGtLtFieldMap.size() > 0) {
            for (Map.Entry<String, Range<?>> entry : rangeGtLtFieldMap.entrySet()) {
                String key = entry.getKey();
                Range<?> range = entry.getValue();
                if (range == null) {
                    continue;
                }
                rangeQueryBuilder4 = QueryBuilders.rangeQuery(key)
                        .gt(range.getMin())
                        .lt(range.getMax());
                boolQueryBuilder.must(rangeQueryBuilder2);
                queryBuilderList.add(rangeQueryBuilder4);
            }
        }
    }

    public static void processExtremeLongValueField(JestClient jestClient, EsSearchParams params, BoolQueryBuilder boolQueryBuilder, List<QueryBuilder> queryBuilderList) {
        ////5. 极值聚合查询
        Map<String, Boolean> extremeFieldMap = params.getExtremeFieldMap();
        if (extremeFieldMap != null && extremeFieldMap.size() > 0) {
            for (Map.Entry<String, Boolean> entry : extremeFieldMap.entrySet()) {
                String fieldName = entry.getKey();
                Boolean ismax = entry.getValue();

                QueryBuilder[] queryBuilders = queryBuilderListToArrays(queryBuilderList);
                if (ismax) {
                    //最大值
                    QueryBuilder maxQueryBuilder = createMaxQueryBuilder(jestClient, params.getIndex(), params.getType(), fieldName, queryBuilders);
                    if (maxQueryBuilder != null) {
                        boolQueryBuilder.must(maxQueryBuilder);
                    }

                } else {
                    //最小值
                    QueryBuilder minQueryBuilder = createMinQueryBuilder(jestClient, params.getIndex(), params.getType(), fieldName, queryBuilders);
                    if (minQueryBuilder != null) {
                        boolQueryBuilder.must(minQueryBuilder);
                    }
                }
            }
        }
    }

    /**
     * 获取一个max极值查询 目前只支持long类型
     *
     * @param jestClient
     * @param index
     * @param type
     * @param fieldName
     * @param queryBuilders
     * @return
     */
    public static QueryBuilder createMaxQueryBuilder(JestClient jestClient, String index, String type, String fieldName, QueryBuilder... queryBuilders) {
        SearchSourceBuilder ssb = new SearchSourceBuilder();
        String maxName = UUID.randomUUID().toString();
        MaxAggregationBuilder maxBuilder = AggregationBuilders.max(maxName).field(fieldName);
        if (queryBuilders != null && queryBuilders.length > 0) {
            BoolQueryBuilder bqb = QueryBuilders.boolQuery();
            for (QueryBuilder tqb : queryBuilders) {
                bqb.must(tqb);
            }
            ssb.query(bqb);
        }
        ssb.aggregation(maxBuilder).size(1);
        String query = ssb.toString();
        //logger.prnln(query);
        Search search = new Search.Builder(query)
                .addIndex(index)
                .addType(type)
                .build();
        SearchResult result = null;
        try {
            result = jestClient.execute(search);
            MaxAggregation newVal = result.getAggregations().getMaxAggregation(maxName);
            Double newValMax = newVal == null ? null : newVal.getMax();
            if (newValMax != null) {
                BigDecimal bigDecimal = new BigDecimal(newValMax);
                Long maxValue = Long.valueOf(bigDecimal.toPlainString());
                return QueryBuilders.termQuery(fieldName, maxValue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取一个max极值查询 目前只支持long类型
     *
     * @param jestClient
     * @param index
     * @param type
     * @param fieldName
     * @param queryBuilders
     * @return
     */
    public static QueryBuilder createMinQueryBuilder(JestClient jestClient, String index, String type, String fieldName, QueryBuilder... queryBuilders) {
        SearchSourceBuilder ssb = new SearchSourceBuilder();
        String minName = UUID.randomUUID().toString();
        MinAggregationBuilder minBuilder = AggregationBuilders.min(minName).field(fieldName);
        if (queryBuilders != null && queryBuilders.length > 0) {
            BoolQueryBuilder bqb = QueryBuilders.boolQuery();
            for (QueryBuilder tqb : queryBuilders) {
                bqb.must(tqb);
            }
            ssb.query(bqb);
        }
        ssb.aggregation(minBuilder).size(1);
        String query = ssb.toString();
        Search search = new Search.Builder(query)
                .addIndex(index)
                .addType(type)
                .build();
        SearchResult result = null;
        try {
            result = jestClient.execute(search);
            MinAggregation newVal = result.getAggregations().getMinAggregation(minName);
            Double newValMin = newVal.getMin();
            if (newValMin != null) {
                BigDecimal bigDecimal = new BigDecimal(newValMin);
                Long minValue = Long.valueOf(bigDecimal.toPlainString());
                return QueryBuilders.termQuery(fieldName, minValue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    //判断参数值是否为空、空字符串或者全空格
    public static boolean checkIsEmpty(Object value) {
        //未传参数
        if (null == value) {
            return true;
        }
        if (value instanceof String) {
            //参数是空字符串
            if ("".equals(value)) {
                return true;
            }
            //参数全部都是空格
            if ("".equals(((String) value).trim())) {
                return true;
            }
        }
        return false;
    }

    //list转数组
    private static QueryBuilder[] queryBuilderListToArrays(List<QueryBuilder> queryBuilderList) {
        if (queryBuilderList == null || queryBuilderList.size() == 0) {
            return null;
        }
        int size = queryBuilderList.size();
        QueryBuilder[] queryBuilders = new QueryBuilder[size];
        for (int i = 0; i < size; i++) {
            queryBuilders[i] = queryBuilderList.get(i);
        }
        return queryBuilders;
    }
}
