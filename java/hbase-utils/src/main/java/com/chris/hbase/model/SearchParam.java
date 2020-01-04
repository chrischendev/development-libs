package com.chris.hbase.model;

/**
 * Create by Chris Chan
 * Create on 2019/3/26 13:53
 * Use for:
 */
public class SearchParam {
    public int index;
    public String keyWords;
    public SearchType searchType;

    public SearchParam() {
    }

    public SearchParam(int index, String keyWords, SearchType searchType) {
        this.index = index;
        this.keyWords = keyWords;
        this.searchType = searchType;
    }

    public SearchParam(String keyWords, SearchType searchType) {
        this.keyWords = keyWords;
        this.searchType = searchType;
    }

    public static SearchParam create(int index, String keyWords, SearchType searchType) {
        return new SearchParam(index, keyWords, searchType);
    }

    public static SearchParam create(String keyWords, SearchType searchType) {
        return new SearchParam(keyWords, searchType);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public SearchType getSearchType() {
        return searchType;
    }

    public void setSearchType(SearchType searchType) {
        this.searchType = searchType;
    }
}
