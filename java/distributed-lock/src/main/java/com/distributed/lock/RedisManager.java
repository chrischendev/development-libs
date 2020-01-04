package com.distributed.lock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Create by Chris Chan
 * Create on 2019/6/13 10:06
 * Use for: 配置中心
 * @author chris
 */
public class RedisManager {
    private String redisHost;
    private int redisPort;
    private String redisAuth;

    private int redisTimeOut = 5000;
    private int redisPoolMaxTotal = 30;
    private int redisPoolMaxIdle = 10;
    private int redisDatabase = 0;

    private JedisPool jedisPool;

    private RedisManager() {
    }

    public static RedisManager get() {
        return new RedisManager();
    }

    public String getRedisHost() {
        return redisHost;
    }

    public RedisManager setRedisHost(String redisHost) {
        this.redisHost = redisHost;
        return this;
    }

    public int getRedisPort() {
        return redisPort;
    }

    public RedisManager setRedisPort(int redisPort) {
        this.redisPort = redisPort;
        return this;
    }

    public String getRedisAuth() {
        return redisAuth;
    }

    public RedisManager setRedisAuth(String redisAuth) {
        this.redisAuth = redisAuth;
        return this;
    }

    public int getRedisTimeOut() {
        return redisTimeOut;
    }

    public RedisManager setRedisTimeOut(int redisTimeOut) {
        this.redisTimeOut = redisTimeOut;
        return this;
    }

    public int getRedisPoolMaxTotal() {
        return redisPoolMaxTotal;
    }

    public RedisManager setRedisPoolMaxTotal(int redisPoolMaxTotal) {
        this.redisPoolMaxTotal = redisPoolMaxTotal;
        return this;
    }

    public int getRedisPoolMaxIdle() {
        return redisPoolMaxIdle;
    }

    public RedisManager setRedisPoolMaxIdle(int redisPoolMaxIdle) {
        this.redisPoolMaxIdle = redisPoolMaxIdle;
        return this;
    }

    public int getRedisDatabase() {
        return redisDatabase;
    }

    public RedisManager setRedisDatabase(int redisDatabase) {
        this.redisDatabase = redisDatabase;
        return this;
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public RedisManager setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
        return this;
    }

    public Jedis getJedis() {
        return this.jedisPool.getResource();
    }

    /**
     * 初始化所有使用者
     *
     * @return
     */
    public RedisManager initAll() {
        buildJedisPool();
        RedisUtils.init(this);
        return this;
    }

    /**
     * 构建redis连接池
     *
     * @return
     */
    private JedisPool buildJedisPool() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        //最大连接数
        jedisPoolConfig.setMaxTotal(redisPoolMaxTotal);
        //最大空闲数
        jedisPoolConfig.setMaxIdle(redisPoolMaxIdle);
        if (null == this.jedisPool) {
            this.jedisPool = new JedisPool(jedisPoolConfig, redisHost, redisPort, redisTimeOut, redisAuth, redisDatabase);
        }
        return this.jedisPool;
    }
}
