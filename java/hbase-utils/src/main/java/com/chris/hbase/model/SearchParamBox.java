package com.chris.hbase.model;


import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Create by Chris Chan
 * Create on 2019/3/26 13:53
 * Use for: 搜索参数
 */
public class SearchParamBox {
    public String tableName;
    public String columnFamily;//固定列簇
    public String column;//固定列 小列
    public List<SearchParam> searchParamList;
    public Range range;
    public String linkSymbol;
    public boolean showSearchString = false;

    public SearchParamBox() {
        this.searchParamList = new ArrayList<>();
        this.range = new Range();
    }

    public static SearchParamBox get() {
        return new SearchParamBox();
    }

    public SearchParamBox(String tableName, List<SearchParam> searchParamList, Range range, String linkSymbol) {
        this.tableName = tableName;
        this.searchParamList = searchParamList;
        this.range = range;
        this.linkSymbol = linkSymbol;
    }

    public SearchParamBox(String tableName, String columnFamily, String column, List<SearchParam> searchParamList, Range range, String linkSymbol) {
        this.tableName = tableName;
        this.columnFamily = columnFamily;
        this.column = column;
        this.searchParamList = searchParamList;
        this.range = range;
        this.linkSymbol = linkSymbol;
    }

    public SearchParamBox(String tableName, String columnFamily, String column, List<SearchParam> searchParamList, Range range, String linkSymbol, boolean showSearchString) {
        this.tableName = tableName;
        this.columnFamily = columnFamily;
        this.column = column;
        this.searchParamList = searchParamList;
        this.range = range;
        this.linkSymbol = linkSymbol;
        this.showSearchString = showSearchString;
    }

    public static SearchParamBox create(String tableName, List<SearchParam> searchParamList, Range range, String linkSymbol) {
        return new SearchParamBox(tableName, searchParamList, range, linkSymbol);
    }

    public String getTableName() {
        return tableName;
    }

    public SearchParamBox setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public List<SearchParam> getSearchParamList() {
        return searchParamList;
    }

    public SearchParamBox setSearchParamList(List<SearchParam> searchParamList) {
        this.searchParamList = searchParamList;
        return this;
    }

    public Range getRange() {
        return range;
    }

    public SearchParamBox setRange(Range range) {
        this.range = range;
        return this;
    }

    public <T> SearchParamBox setRange(T min, T max) {
        if (null == min || null == max) {
            return this;
        }
        this.range = Range.create(min, max);
        return this;
    }

    public String getLinkSymbol() {
        return linkSymbol;
    }

    public SearchParamBox setLinkSymbol(String linkSymbol) {
        this.linkSymbol = linkSymbol;
        return this;
    }

    public boolean isShowSearchString() {
        return showSearchString;
    }

    public String getColumnFamily() {
        return columnFamily;
    }

    public SearchParamBox setColumnFamily(String columnFamily) {
        this.columnFamily = columnFamily;
        return this;
    }

    public String getColumn() {
        return column;
    }

    public SearchParamBox setColumn(String column) {
        this.column = column;
        return this;
    }

    public SearchParamBox setShowSearchString(boolean showSearchString) {
        this.showSearchString = showSearchString;
        return this;
    }

    public SearchParamBox addSearchParam(SearchParam searchParam) {
        if (searchParam == null) {
            return this;
        }
        if (this.searchParamList == null) {
            this.searchParamList = new ArrayList<>();
        }
        this.searchParamList.add(searchParam);
        return this;
    }

    public SearchParamBox addSearchParam(SearchType searchType) {
        return addSearchParam(null, searchType);
    }

    public SearchParamBox addSearchParam(String keyWords, SearchType searchType) {
        //检查index重复
        if (StringUtils.isBlank(keyWords) || null == searchType) {
            return this;
        }
        if (this.searchParamList == null) {
            this.searchParamList = new ArrayList<>();
        }
        this.searchParamList.add(SearchParam.create(keyWords, searchType));
        return this;
    }

    public SearchParamBox addSearchParam(int index, String keyWords, SearchType searchType) {
        //检查index重复
        Set<Integer> indexSet = this.searchParamList.stream().map(SearchParam::getIndex).collect(Collectors.toSet());
        if (indexSet.contains(index)) {
            throw new RuntimeException("index有重复");
        }
        if (StringUtils.isBlank(keyWords) || null == searchType) {
            return this;
        }
        if (this.searchParamList == null) {
            this.searchParamList = new ArrayList<>();
        }
        this.searchParamList.add(SearchParam.create(index, keyWords, searchType));
        return this;
    }
}
