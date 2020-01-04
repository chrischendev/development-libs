package com.distributed.lock;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;

import java.util.Map;

/**
 * Create by Chris Chan
 * Create on 2019/5/9 14:30
 * Use for: redis读写工具
 * @author chris
 */
public class RedisUtils {
    private static RedisManager redisManager;
    private static Gson gson = new Gson();

    public static void init(RedisManager redisManager) {
        RedisUtils.redisManager = redisManager;
    }

    //读取数据
    public static <T> T readData(String key, Class<T> clazz) {
        Jedis jedis = redisManager.getJedis();
        String jsonStr = jedis.get(key);
        if (StringUtils.isBlank(jsonStr)) {
            return null;
        }
        T data = gson.fromJson(jsonStr, clazz);
        //jedis.close();
        return data;
    }

    //写入数据
    public static <T> void writeDataMap(Map<String, T> dataMap) {
        Jedis jedis = redisManager.getJedis();
        dataMap.forEach((key, data) -> jedis.set(key, gson.toJson(data)));
        //jedis.close();
    }

    //读取数据 按照field为租户id进行hash读取
    public static <T> T readDataByTenant(String key, String tenantId, Class<T> clazz) {
        Jedis jedis = redisManager.getJedis();
        String jsonStr = jedis.hget(key, tenantId);
        if (StringUtils.isEmpty(jsonStr)) {
            return null;
        }
        T data = gson.fromJson(jsonStr, clazz);
        //jedis.close();
        return data;
    }
}
