package com.chris.framework.builder.libs.springboot.repository;

import com.chris.framework.builder.model.PageModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * CustomRepositoryDemo
 * library
 * Created by Chris Chen
 * 2017/9/15
 * Explain:自定义的Repository接口
 */
@NoRepositoryBean
public interface BaseRepository<T> extends JpaRepository<T, Integer> {
    /**
     * 获得最后插入的记录的id
     *
     * @return
     */
    Integer getLastInsertId();

    /**
     * 获取自定义分页数据
     *
     * @param pageable
     * @return
     */
    PageModel<T> getPage(Pageable pageable);

}
