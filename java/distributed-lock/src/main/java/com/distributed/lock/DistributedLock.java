package com.distributed.lock;

import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;

/**
 * Create by Chris Chan
 * Create on 2019/6/13 10:46
 * Use for: 分布式锁
 */
public class DistributedLock {
    private static Jedis mJedis;
    //加锁
    private static String LOCK = "1";
    //释放锁
    private static String UNLOCK = "0";

    public static void init(Jedis jedis) {
        DistributedLock.mJedis = jedis;
    }

    /**
     * 获取锁
     *
     * @param key
     * @param expx
     * @param time
     * @return
     */
    public static boolean lock(String key, String expx, long time) {
        String locked = mJedis.set(key, LOCK, "NX", expx, time);
        //System.out.println(key + " locked.");
        return null != locked;
    }

    /**
     * 释放锁
     *
     * @param key
     * @return
     */
    public static boolean unLock(String key) {
        //return null != jedis.set(key, UNLOCK);
        Long del = mJedis.del(key);
        //System.out.println(key + " unlocked.");
        return null != del;
    }

    /**
     * 读取锁状态 是否空闲
     *
     * @param key
     * @return 是否空闲
     */
    public static boolean isLockFree(String key) {
        String lockValue = mJedis.get(key);
        if (StringUtils.isBlank(lockValue)) {
            //如果还没有这个锁，返回锁空闲
            return true;
        }
        return UNLOCK.equals(lockValue);
    }

    /**
     * 尝试获取锁
     *
     * @param key
     * @param expx
     * @param time
     * @return
     */
    public static boolean tryLock(String key, String expx, long time) {
        while (true) {
            if (isLockFree(key)) {
                lock(key, expx, time);
                return true;
            }
        }
    }
}
