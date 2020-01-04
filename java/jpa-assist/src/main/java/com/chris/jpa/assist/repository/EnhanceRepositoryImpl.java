package com.chris.jpa.assist.repository;


import com.chris.jpa.assist.model.PageData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * CustomRepositoryDemo
 * library.custom
 * Created by Chris Chen
 * 2017/9/15
 * Explain:
 */
public class EnhanceRepositoryImpl<T, ID extends Serializable>
        extends SimpleJpaRepository<T, ID>
        implements EnhanceRepository<T, ID> {

    private EntityManager entityManager;

    public EnhanceRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public Integer getLastInsertId() {
        Object result = entityManager.createNativeQuery("SELECT LAST_INSERT_ID()").getSingleResult();
        return Integer.valueOf(String.valueOf(result));
    }

    /**
     * 获取自定义分页
     *
     * @param pageable
     * @return
     */
    @Override
    public PageData<T> getPage(Pageable pageable) {
        PageData<T> pageData = new PageData<T>(pageable.getPageNumber() + 1, pageable.getPageSize());
        pageData.count = count();
        pageData.hasNext = pageData.page * pageData.pageSize < pageData.count;//是否有下一页
        pageData.dataList = findAll(pageable).getContent();
        return pageData;
    }

    @Override
    public boolean batchSave(T[] datas) {
        return batchSave(Arrays.asList(datas));
    }

    @Override
    public boolean batchSave(Collection<T> dataCollection) {
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

    @Override
    public boolean batchSave(T[] datas, int batchSize) {
        return batchSave(Arrays.asList(datas), batchSize);
    }

    @Override
    public boolean batchSave(Collection<T> dataCollection, int batchSize) {
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

    @Override
    public boolean batchSaveList(List<T> dataList, int batchSize) {
        int size = dataList.size();
        for (int i = 0; i < size; i += batchSize) {
            int toIndex = size - i < batchSize ? i + size - i : i + batchSize;
            dataList.subList(i, toIndex).stream().forEach(entity -> entityManager.persist(entity));
            entityManager.flush();
            entityManager.clear();
        }
        return true;
    }


//    /**
//     * 批量写入
//     *
//     * @param entities
//     * @param <S>
//     * @return
//     */
//    @Override
//    public <S extends T> List<S> batchSave(Iterable<S> entities) {
//        List<S> result = new ArrayList();
//        if (entities == null) {
//            return result;
//        } else {
//            Iterator<S> iterator = entities.iterator();
//
//            while (iterator.hasNext()) {
//                S entity = iterator.next();
//                entityManager.persist(entity);
//            }
//            entityManager.flush();
//            entityManager.clear();
//            return result;
//        }
//    }
//
//    /**
//     * 分批写入
//     *
//     * @param entities
//     * @param batchSize 分批大小
//     * @param <S>
//     * @return
//     */
//    @Override
//    public <S extends T> List<S> batchSave(Iterable<S> entities, int batchSize) {
//        List<S> result = new ArrayList();
//        if (entities == null) {
//            return result;
//        } else {
//            Iterator<S> iterator = entities.iterator();
//            long count = 0;//计数器
//            while (iterator.hasNext()) {
//                S entity = iterator.next();
//                entityManager.persist(entity);
//                count++;
//                if (count >= batchSize) {
//                    entityManager.flush();
//                    entityManager.clear();
//                    count = 0;//计数器归零
//                }
//            }
//            //提交零头
//            if (count > 0) {
//                entityManager.flush();
//                entityManager.clear();
//            }
//            return result;
//        }
//    }


}
