package com.chris.es.jest.utils;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Chris Chen
 * 2018/11/22
 * Explain:
 */

public class JestCommonUtil {
    private JestClient jestClient;

    public <T> boolean save(T data, String index, String type) throws IOException {
        return ((JestProcessor<T>) () -> jestClient).save(data, index, type);
    }

    public <T> boolean save(T data, String indexAndtype) throws IOException {
        return ((JestProcessor<T>) () -> jestClient).save(data, indexAndtype, indexAndtype);
    }

    public <T> boolean saveAll(List<T> dataList, String index, String type) throws IOException {
        return ((JestProcessor<T>) () -> jestClient).saveAll(dataList, index, type);
    }

    public <T> boolean saveAll(List<T> dataList, String indexAndtype) throws IOException {
        return ((JestProcessor<T>) () -> jestClient).saveAll(dataList, indexAndtype, indexAndtype);
    }

    public <T> boolean update(T data, String index, String type, String id) throws IOException {
        return ((JestProcessor<T>) () -> jestClient).update(data, index, type, id);
    }

    public <T> boolean updateAll(List<T> dataList, String index, String type, String idFieldName) throws IOException {
        return ((JestProcessor<T>) () -> jestClient).updateAll(dataList, index, type, idFieldName);
    }

    public <T> boolean updateAll(List<T> dataList, String indexAndtype, String idFieldName) throws IOException {
        return ((JestProcessor<T>) () -> jestClient).updateAll(dataList, indexAndtype, idFieldName);
    }

    public <T> boolean update(T data, String indexAndtype, String id) throws IOException {
        return ((JestProcessor<T>) () -> jestClient).update(data, indexAndtype, indexAndtype, id);
    }

    public long getMaxResultWindow(String index) throws IOException {
        return ((JestProcessor) () -> jestClient).getMaxResultWindow(index);
    }

    public boolean setMaxResultWindow(String index, long maxResult) throws IOException {
        return ((JestProcessor) () -> jestClient).setMaxResultWindow(index, maxResult);
    }

    public <T> boolean delete(String index, String type, String id) throws IOException {
        return ((JestProcessor<T>) () -> jestClient).delete(index, type, id);
    }

    public <T> boolean delete(String indexAndtype, String id) throws IOException {
        return ((JestProcessor<T>) () -> jestClient).delete(indexAndtype, indexAndtype, id);
    }

    public <T> boolean deleteIndex(String index) throws IOException {
        return ((JestProcessor<T>) () -> jestClient).deleteIndex(index);
    }

    public <T> List<T> findAll(Class<T> clazz, String index, String type) throws IOException {
        return ((JestProcessor<T>) () -> jestClient).findAll(clazz, index, type);
    }

    public <T> List<T> findAll(Class<T> clazz, String indexAndtype) throws IOException {
        return ((JestProcessor<T>) () -> jestClient).findAll(clazz, indexAndtype, indexAndtype);
    }

    public JestClient getJestClient() {
        return jestClient;
    }

    public void setJestClient(JestClient client) {
        this.jestClient = client;
    }

    public static JestCommonUtil create() {
        return new JestCommonUtil();
    }

    public static JestCommonUtil create(JestClient client) {
        JestCommonUtil jestCommonUtil = new JestCommonUtil();
        jestCommonUtil.jestClient = client;
        return jestCommonUtil;
    }

    public JestClient init(String serverUri, int timeout) {
        return init(timeout, serverUri);
    }

    public JestClient init(int timeout, String serverUri) {
        return init(timeout, new String[]{serverUri});
    }

    public JestClient init(int timeout, String... serverUris) {
        return init(timeout, Arrays.asList(serverUris));
    }

    public JestClient init(String[] serverUris, int timeout) {
        return init(timeout, serverUris);
    }

    public JestClient init(int timeout, List<String> serverUriList) {
        JestClientFactory jestClientFactory = new JestClientFactory();
        HttpClientConfig config = new HttpClientConfig.Builder(serverUriList)
                .connTimeout(1000 * timeout)
                .multiThreaded(true)
                .build();
        jestClientFactory.setHttpClientConfig(config);
        this.jestClient = jestClientFactory.getObject();
        return jestClient;
    }

    @Deprecated
    public JestClient createJestClient(String serverUri, int timeout) {
        return init(serverUri, timeout);
    }
}


