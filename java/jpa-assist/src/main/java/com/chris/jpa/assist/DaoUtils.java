package com.chris.jpa.assist;

import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Create by Chris Chan
 * Create on 2019/3/27 16:09
 * Use for:
 */
public class DaoUtils {
    private static EntityManager entityManager;

    /**
     * 初始化 传递EntityManager 一个服务只需要执行一次
     * @param em
     */
    public static void init(EntityManager em) {
        DaoUtils.entityManager = em;
    }

    public static EntityManager getEntityManager() {
        return DaoUtils.entityManager;
    }

    public static void setEntityManager(EntityManager entityManager) {
        DaoUtils.entityManager = entityManager;
    }

    public static <T> boolean batchSave(T[] datas) {
        return batchSave(Arrays.asList(datas));
    }

    public static <T> boolean batchSave(Collection<T> dataCollection) {
        if (CollectionUtils.isEmpty(dataCollection)) {
            return false;
        }
        dataCollection.stream().forEach(data -> {
            entityManager.persist(data);
        });
        entityManager.flush();
        entityManager.clear();
        return true;
    }

    public static <T> boolean batchSave(T[] datas, int batchSize) {
        return batchSave(Arrays.asList(datas), batchSize);
    }

    public static <T> boolean batchSave(Collection<T> dataCollection, int batchSize) {
        if (CollectionUtils.isEmpty(dataCollection)) {
            return false;
        }
        long count = 0;//计数器
        for (T data : dataCollection) {
            entityManager.persist(data);
            count++;
            if (count >= batchSize) {
                entityManager.flush();
                entityManager.clear();
                count = 0;//计数器归零
            }
        }
        //提交零头
        if (count > 0) {
            entityManager.flush();
            entityManager.clear();
        }
        return true;
    }

    public static <T> boolean batchSaveList(List<T> dataList, int batchSize) {
        int size = dataList.size();
        for (int i = 0; i < size; i += batchSize) {
            int toIndex = size - i < batchSize ? i + size - i : i + batchSize;
            dataList.subList(i, toIndex).stream().forEach(entity -> entityManager.persist(entity));
            entityManager.flush();
            entityManager.clear();
        }
        return true;
    }
}
