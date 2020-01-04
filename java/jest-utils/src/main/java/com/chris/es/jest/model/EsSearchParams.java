package com.chris.es.jest.model;

import io.searchbox.core.search.sort.Sort;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chris Chen
 * 2018/10/15
 * Explain: 对ElasticSearch进行多条件分页搜索的参数类
 */

public class EsSearchParams extends PageParams {
    private Map<String, Object> fieldMap;//单字段单值精确匹配映射
    private Map<String, Object[]> fieldMultiValueMap;//单字段多值精确匹配映射 可以与上统一处理
    private Map<String, String> mustWildcardFieldMap;//单字段模糊匹配映射 must
    private Map<String, String> shouldWildcardFieldMap;//单字段模糊匹配映射 should
    private Map<String, String[]> multiFieldMap;//多字段同值精确匹配映射
    private Map<String, String[]> multiWildcardFieldMap;//多字段同值模糊匹配映射
    private Map<String, Range<?>> rangeFieldMap;//全闭合值区间搜索参数
    private Map<String, Range<?>> rangeGtLteFieldMap;//左开右闭值区间搜索参数
    private Map<String, Range<?>> rangeGteLtFieldMap;//左闭右开 值区间搜索参数
    private Map<String, Range<?>> rangeGtLtFieldMap;//全开值区间搜索参数
    private Map<String, Boolean> extremeFieldMap;//极值搜索参数
    private String index;//es index 数据库
    private String type;//es type 数据表
    private String sortFieldName;//排序字段
    private Sort.Sorting sortMode = Sort.Sorting.ASC;//排序方式
    private String[] sourceFields;//查询结果的字段
    private boolean showQueryString = false;//是否显示 queryString字符串

    public EsSearchParams() {
    }

    public static EsSearchParams get() {
        return new EsSearchParams();
    }

    public Map<String, Object> getFieldMap() {
        return fieldMap;
    }

    public EsSearchParams setFieldMap(Map<String, Object> fieldMap) {
        this.fieldMap = fieldMap;
        return this;
    }

    public Map<String, Object[]> getFieldMultiValueMap() {
        return fieldMultiValueMap;
    }

    public void setFieldMultiValueMap(Map<String, Object[]> fieldMultiValueMap) {
        this.fieldMultiValueMap = fieldMultiValueMap;
    }

    public Map<String, String> getMustWildcardFieldMap() {
        return mustWildcardFieldMap;
    }

    public void setMustWildcardFieldMap(Map<String, String> mustWildcardFieldMap) {
        this.mustWildcardFieldMap = mustWildcardFieldMap;
    }

    public EsSearchParams addMustWildcardField(String key, String keyWords) {
        if (this.mustWildcardFieldMap == null) {
            this.mustWildcardFieldMap = new HashMap<>();
        }
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(keyWords)) {
            return this;
        }
        this.mustWildcardFieldMap.put(key, keyWords);
        return this;
    }

    public Map<String, String> getShouldWildcardFieldMap() {
        return shouldWildcardFieldMap;
    }

    public EsSearchParams setShouldWildcardFieldMap(Map<String, String> shouldWildcardFieldMap) {
        this.shouldWildcardFieldMap = shouldWildcardFieldMap;
        return this;
    }

    public EsSearchParams addShouldWildcardField(String key, String keyWords) {
        if (this.shouldWildcardFieldMap == null) {
            this.shouldWildcardFieldMap = new HashMap<>();
        }
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(keyWords)) {
            return this;
        }
        this.shouldWildcardFieldMap.put(key, keyWords);
        return this;
    }

    public Map<String, String[]> getMultiFieldMap() {
        return multiFieldMap;
    }

    public EsSearchParams setMultiFieldMap(Map<String, String[]> multiFieldMap) {
        this.multiFieldMap = multiFieldMap;
        return this;
    }

    public Map<String, String[]> getMultiWildcardFieldMap() {
        return multiWildcardFieldMap;
    }

    public EsSearchParams setMultiWildcardFieldMap(Map<String, String[]> multiWildcardFieldMap) {
        this.multiWildcardFieldMap = multiWildcardFieldMap;
        return this;
    }

    public EsSearchParams addMultiWildcardField(String val, String... fields) {
        if (this.multiWildcardFieldMap == null) {
            this.multiWildcardFieldMap = new HashMap<>();
        }
        this.multiWildcardFieldMap.put(val, fields);
        return this;
    }

    public String getIndex() {
        return index;
    }

    public EsSearchParams setIndex(String index) {
        this.index = index;
        return this;
    }

    public String getType() {
        return type;
    }

    public EsSearchParams setType(String type) {
        this.type = type;
        return this;
    }

    public EsSearchParams setIndexAndType(String index, String type) {
        this.index = index;
        this.type = type;
        return this;
    }

    public EsSearchParams setIndexAndType(String indexAndtype) {
        this.index = indexAndtype;
        this.type = indexAndtype;
        return this;
    }

    public EsSearchParams addFieldKV(String fieldName, Object valWord) {
        if (this.fieldMap == null) {
            this.fieldMap = new HashMap<>();
        }
        if (valWord instanceof String && (StringUtils.isEmpty(fieldName) || StringUtils.isEmpty(valWord))) {
            return this;
        }
        if (StringUtils.isEmpty(fieldName.trim()) || (valWord instanceof String && (StringUtils.isEmpty(String.valueOf(valWord).trim())))) {
            return this;
        }
        this.fieldMap.put(fieldName, valWord);
        return this;
    }

    public EsSearchParams addFieldMultiValueKV(String fieldName, Object... valWords) {
        if (this.fieldMultiValueMap == null) {
            this.fieldMultiValueMap = new HashMap<>();
        }
        if (valWords == null || valWords.length == 0) {
            return this;
        }
        this.fieldMultiValueMap.put(fieldName, valWords);
        return this;
    }

    public EsSearchParams addMultiFieldKV(String valWord, String... multiFields) {
        if (this.multiFieldMap == null) {
            this.multiFieldMap = new HashMap<>();
        }
        if (valWord == null) {
            return this;
        }
        this.multiFieldMap.put(valWord, multiFields);
        return this;
    }

    public EsSearchParams addMultiFieldKV(String valWord, List<String> multiFieldList) {
        if (this.multiFieldMap == null) {
            this.multiFieldMap = new HashMap<>();
        }
        if (multiFieldList == null || multiFieldList.size() == 0) {
            return this;
        }
        this.multiFieldMap.put(valWord, (String[]) multiFieldList.toArray());
        return this;
    }

    public EsSearchParams setPageParams(PageParams pageParams) {
        setPage(pageParams.getPage());
        setPageSize(pageParams.getPageSize());
        return this;
    }

    public EsSearchParams setPageParams(int page, int pageSize) {
        setPage(page);
        setPageSize(pageSize);
        return this;
    }

    public Map<String, Range<?>> getRangeFieldMap() {
        return rangeFieldMap;
    }

    public EsSearchParams setRangeFieldMap(Map<String, Range<?>> rangeFieldMap) {
        this.rangeFieldMap = rangeFieldMap;
        return this;
    }

    public EsSearchParams addRangeField(String fieldname, Range<?> range) {
        if (this.rangeFieldMap == null) {
            this.rangeFieldMap = new HashMap<>();
        }
        if (range == null) {
            return this;
        }
        this.rangeFieldMap.put(fieldname, range);
        return this;
    }

    public EsSearchParams addRangeField(String fieldname, Object min, Object max) {
        if (this.rangeFieldMap == null) {
            this.rangeFieldMap = new HashMap<>();
        }
        this.rangeFieldMap.put(fieldname, new Range<>(min, max));
        return this;
    }

    public Map<String, Boolean> getExtremeFieldMap() {
        return extremeFieldMap;
    }

    public EsSearchParams setExtremeFieldMap(Map<String, Boolean> extremeFieldMap) {
        this.extremeFieldMap = extremeFieldMap;
        return this;
    }

    public EsSearchParams addExtremeField(String fieldName, Boolean isMax) {
        if (this.extremeFieldMap == null) {
            this.extremeFieldMap = new HashMap<>();
        }
        this.extremeFieldMap.put(fieldName, isMax);
        return this;
    }

    public String getSortFieldName() {
        return sortFieldName;
    }

    public EsSearchParams setSortFieldName(String sortFieldName) {
        this.sortFieldName = sortFieldName;
        return this;
    }

    public Sort.Sorting getSortMode() {
        return sortMode;
    }

    public EsSearchParams setSortMode(Sort.Sorting sortMode) {
        this.sortMode = sortMode;
        return this;
    }

    public EsSearchParams setSort(String sortFieldName, boolean isAsc) {
        this.sortFieldName = sortFieldName;
        if (isAsc) {
            this.sortMode = Sort.Sorting.ASC;
        } else {
            this.sortMode = Sort.Sorting.DESC;
        }
        return this;
    }

    public Map<String, Range<?>> getRangeGtLteFieldMap() {
        return rangeGtLteFieldMap;
    }

    public EsSearchParams setRangeGtLteFieldMap(Map<String, Range<?>> rangeGtLteFieldMap) {
        this.rangeGtLteFieldMap = rangeGtLteFieldMap;
        return this;
    }

    public EsSearchParams addRangeGtLteField(String fieldname, Object min, Object max) {
        if (this.rangeGtLteFieldMap == null) {
            this.rangeGtLteFieldMap = new HashMap<>();
        }
        this.rangeGtLteFieldMap.put(fieldname, new Range<>(min, max));
        return this;
    }

    public Map<String, Range<?>> getRangeGteLtFieldMap() {
        return rangeGteLtFieldMap;
    }

    public EsSearchParams setRangeGteLtFieldMap(Map<String, Range<?>> rangeGteLtFieldMap) {
        this.rangeGteLtFieldMap = rangeGteLtFieldMap;
        return this;
    }

    public EsSearchParams addRangeGteLtField(String fieldname, Object min, Object max) {
        if (this.rangeGteLtFieldMap == null) {
            this.rangeGteLtFieldMap = new HashMap<>();
        }
        this.rangeGteLtFieldMap.put(fieldname, new Range<>(min, max));
        return this;
    }

    public Map<String, Range<?>> getRangeGtLtFieldMap() {
        return rangeGtLtFieldMap;
    }

    public EsSearchParams setRangeGtLtFieldMap(Map<String, Range<?>> rangeGtLtFieldMap) {
        this.rangeGtLtFieldMap = rangeGtLtFieldMap;
        return this;
    }

    public EsSearchParams addRangeGtLtField(String fieldname, Object min, Object max) {
        if (this.rangeGtLtFieldMap == null) {
            this.rangeGtLtFieldMap = new HashMap<>();
        }
        this.rangeGtLtFieldMap.put(fieldname, new Range<>(min, max));
        return this;
    }

    public String[] getSourceFields() {
        return sourceFields;
    }

    public EsSearchParams setSourceFields(String... sourceFields) {
        this.sourceFields = sourceFields;
        return this;
    }

    public EsSearchParams setReaultClass(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        int length = fields.length;
        this.sourceFields = new String[length];
        for (int i = 0; i < length; i++) {
            this.sourceFields[i] = fields[i].getName();
        }
        return this;
    }

    public boolean isShowQueryString() {
        return showQueryString;
    }

    public EsSearchParams setShowQueryString(boolean showQueryString) {
        this.showQueryString = showQueryString;
        return this;
    }
}
