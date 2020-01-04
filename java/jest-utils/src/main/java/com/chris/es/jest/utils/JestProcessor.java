package com.chris.es.jest.utils;

import com.google.gson.JsonElement;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.*;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.settings.GetSettings;
import io.searchbox.indices.settings.UpdateSettings;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Jest处理工具
 * 仅包含基本的CRUD
 *
 * @param <T>
 */
public interface JestProcessor<T> {
    JestClient getJestClient();

    /**
     * 保存文档
     *
     * @param entity
     * @param index
     * @param type
     * @return
     * @throws IOException
     */
    default boolean save(T entity, String index, String type) throws IOException {
        Index _index = new Index.Builder(entity).index(index).type(type).build();
        DocumentResult documentResult = getJestClient().execute(_index);
        return documentResult.isSucceeded();
    }

    /**
     * 更新数据
     *
     * @param entity
     * @param index
     * @param type
     * @param id
     * @return
     * @throws IOException
     */
    default boolean update(T entity, String index, String type, String id) throws IOException {
        Index _index = new Index.Builder(entity).index(index).type(type).id(id).build();
        //Update update = new Update.Builder(entity).index(index).type(type).id(id).build();
        DocumentResult documentResult = getJestClient().execute(_index);
        return documentResult.isSucceeded();
    }

    /**
     * 设置最大查询结果数
     *
     * @param index
     * @param maxResult
     * @return
     * @throws IOException
     */
    default boolean setMaxResultWindow(String index, long maxResult) throws IOException {
        //XContentBuilder xContentBuilder = XContentFactory.jsonBuilder();
        //xContentBuilder.startObject().field("max_result_window", maxResult).endObject();
        //xContentBuilder.generator().writeString(source);
        //新版本导致原思路不行，简直是逼上梁山，幸亏json结构不复杂。
        String source = new StringBuilder("{\"max_result_window\":").append(maxResult).append("}").toString();
        UpdateSettings build = new UpdateSettings.Builder(source).addIndex(index).setParameter("preserve_existing", false).build();
        JestResult jestResult = getJestClient().execute(build);
        return jestResult.isSucceeded();
    }

    /**
     * 获取最大查询结果数
     *
     * @param index
     * @return
     * @throws IOException
     */
    default long getMaxResultWindow(String index) throws IOException {
        long defaultMaxResult = 10000;//默认最大读取量为10000
        GetSettings build = new GetSettings.Builder().addIndex(index).build();
        JestResult jestResult = getJestClient().execute(build);
        try {
            JsonElement jsonElement = jestResult.getJsonObject()
                    .get(index).getAsJsonObject()
                    .get("settings")
                    .getAsJsonObject()
                    .get("index").getAsJsonObject()
                    .get("max_result_window");
            long maxResult = jsonElement == null ? defaultMaxResult : jsonElement
                    .getAsLong();
            return maxResult;
        } catch (RuntimeException e) {
            return defaultMaxResult;
        }
    }

    /**
     * 删除数据
     *
     * @param index
     * @param type
     * @param id
     * @return
     * @throws IOException
     */
    default boolean delete(String index, String type, String id) throws IOException {
        Delete delete = new Delete.Builder(id).index(index).type(type).build();
        DocumentResult documentResult = getJestClient().execute(delete);
        return documentResult.isSucceeded();
    }

    /**
     * 删除索引
     *
     * @param index
     * @return
     * @throws IOException
     */
    default boolean deleteIndex(String index) throws IOException {
        DeleteIndex deleteIndex = new DeleteIndex.Builder(index).build();
        JestResult jestResult = getJestClient().execute(deleteIndex);
        return jestResult.isSucceeded();
    }

    /**
     * 批量保存文档
     *
     * @param entitys
     * @param index
     * @param type
     * @return
     * @throws IOException
     */
    default boolean saveAll(List<T> entitys, String index, String type) throws IOException {
        Bulk.Builder _bulk = new Bulk.Builder();

        if (Optional.ofNullable(entitys).isPresent()) {
            entitys.stream().forEach(value -> {
                Index _index = new Index.Builder(value).index(index).type(type).build();
                _bulk.addAction(_index);
            });
        }

        BulkResult bulkResult = getJestClient().execute(_bulk.build());
        return bulkResult.isSucceeded();
    }

    /**
     * @param entitys
     * @param index
     * @param type
     * @param idFieldName 数据对象中暂存id的字段名称
     * @return
     * @throws IOException
     */
    default boolean updateAll(List<T> entitys, String index, String type, String idFieldName) throws IOException {
        Bulk.Builder _bulk = new Bulk.Builder();

        if (Optional.ofNullable(entitys).isPresent()) {
            entitys.stream().forEach(value -> {
                //尝试获取id
                String id = null;
                try {
                    Field idField = value.getClass().getDeclaredField(idFieldName);
                    if (idField == null) {
                        return;
                    }
                    try {
                        idField.setAccessible(true);
                        id = (String) idField.get(value);
                        idField.setAccessible(false);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                if (id == null) {
                    return;
                }
                //Update _update = new Update.Builder(value).index(index).type(type).id(id).build();
                Index _index = new Index.Builder(value).index(index).type(type).id(id).build();
                _bulk.addAction(_index);
            });
        }

        BulkResult bulkResult = getJestClient().execute(_bulk.build());
        return bulkResult.isSucceeded();
    }

    default boolean updateAll(List<T> entitys, String indexAndtype, String idFieldName) throws IOException {
        return updateAll(entitys, indexAndtype, indexAndtype, idFieldName);
    }

    /**
     * 批量删除文档
     *
     * @param ids
     * @param index
     * @param type
     * @return
     * @throws IOException
     */
    default boolean deleteAll(List<String> ids, String index, String type) throws IOException {
        Bulk.Builder _bulk = new Bulk.Builder();

        if (Optional.ofNullable(ids).isPresent()) {
            ids.stream().forEach(id -> {
                Delete _delete = new Delete.Builder(id).index(index).type(type).build();
                _bulk.addAction(_delete);
            });
        }

        BulkResult bulkResult = getJestClient().execute(_bulk.build());
        return bulkResult.isSucceeded();
    }

    default boolean deleteAll(List<String> ids, String indexAndtype) throws IOException {
        return deleteAll(ids, indexAndtype, indexAndtype);
    }

    /**
     * 查询所有文档
     *
     * @return
     */
    default List<T> findAll(Class<T> clazz, String index, String type) throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery()); //match_all
        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(index).addType(type).build();
        SearchResult result = getJestClient().execute(search);

        if (result.isSucceeded()) {
            List<SearchResult.Hit<T, Void>> hits = result.getHits(clazz);
            if (hits != null) {
                return hits.stream().map(hit -> hit.source).collect(Collectors.toList());
            }
        }
        return null;
    }
}
