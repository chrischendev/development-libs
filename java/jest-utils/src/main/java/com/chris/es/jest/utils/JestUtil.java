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

public class JestUtil {
    private static JestClient jestClient;

    public static <T> boolean save(T data, String index, String type) throws IOException {
        return ((JestProcessor<T>) () -> jestClient).save(data, index, type);
    }

    public static <T> boolean save(T data, String indexAndtype) throws IOException {
        return ((JestProcessor<T>) () -> jestClient).save(data, indexAndtype, indexAndtype);
    }

    public static <T> boolean saveAll(List<T> dataList, String index, String type) throws IOException {
        return ((JestProcessor<T>) () -> jestClient).saveAll(dataList, index, type);
    }

    public static <T> boolean saveAll(List<T> dataList, String indexAndtype) throws IOException {
        return ((JestProcessor<T>) () -> jestClient).saveAll(dataList, indexAndtype, indexAndtype);
    }

    public static <T> boolean update(T data, String index, String type, String id) throws IOException {
        return ((JestProcessor<T>) () -> jestClient).update(data, index, type, id);
    }

    public static <T> boolean updateAll(List<T> dataList, String index, String type, String idFieldName) throws IOException {
        return ((JestProcessor<T>) () -> jestClient).updateAll(dataList, index, type, idFieldName);
    }

    public static <T> boolean updateAll(List<T> dataList, String indexAndtype, String idFieldName) throws IOException {
        return ((JestProcessor<T>) () -> jestClient).updateAll(dataList, indexAndtype, idFieldName);
    }

    public static <T> boolean update(T data, String indexAndtype, String id) throws IOException {
        return ((JestProcessor<T>) () -> jestClient).update(data, indexAndtype, indexAndtype, id);
    }

    public static long getMaxResultWindow(String index) throws IOException {
        return ((JestProcessor) () -> jestClient).getMaxResultWindow(index);
    }

    public static boolean setMaxResultWindow(String index, long maxResult) throws IOException {
        return ((JestProcessor) () -> jestClient).setMaxResultWindow(index, maxResult);
    }

    public static <T> boolean delete(String index, String type, String id) throws IOException {
        return ((JestProcessor<T>) () -> jestClient).delete(index, type, id);
    }

    public static <T> boolean delete(String indexAndtype, String id) throws IOException {
        return ((JestProcessor<T>) () -> jestClient).delete(indexAndtype, indexAndtype, id);
    }

    public static <T> boolean deleteIndex(String index) throws IOException {
        return ((JestProcessor<T>) () -> jestClient).deleteIndex(index);
    }

    public static <T> List<T> findAll(Class<T> clazz, String index, String type) throws IOException {
        return ((JestProcessor<T>) () -> jestClient).findAll(clazz, index, type);
    }

    public static <T> List<T> findAll(Class<T> clazz, String indexAndtype) throws IOException {
        return ((JestProcessor<T>) () -> jestClient).findAll(clazz, indexAndtype, indexAndtype);
    }

    public static JestClient getJestClient() {
        return jestClient;
    }

    public static void setJestClient(JestClient client) {
        JestUtil.jestClient = client;
    }

    public static void init(JestClient client) {
        JestUtil.jestClient = client;
    }

    public static JestClient init(String serverUri, int timeout) {
        return init(timeout, serverUri);
    }

    public static JestClient init(int timeout, String serverUri) {
        return init(timeout, new String[]{serverUri});
    }

    public static JestClient init(int timeout, String... serverUris) {
        return init(timeout, Arrays.asList(serverUris));
    }

    public static JestClient init(String[] serverUris, int timeout) {
        return init(timeout, serverUris);
    }

    public static JestClient init(int timeout, List<String> serverUriList) {
        HttpClientConfig config = new HttpClientConfig.Builder(serverUriList)
                .connTimeout(1000 * timeout)
                .readTimeout(1000 * timeout)
                .multiThreaded(true)
                .build();
        return init(config);
    }

    public static JestClient init(HttpClientConfig config) {
        JestClientFactory jestClientFactory = new JestClientFactory();
        jestClientFactory.setHttpClientConfig(config);
        JestUtil.jestClient = jestClientFactory.getObject();
        return jestClient;
    }

    @Deprecated
    public static JestClient createJestClient(String serverUri, int timeout) {
        return init(serverUri, timeout);
    }
}


