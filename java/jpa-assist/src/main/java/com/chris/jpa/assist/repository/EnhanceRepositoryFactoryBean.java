package com.chris.jpa.assist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * CustomRepositoryDemo
 * library
 * Created by Chris Chen
 * 2017/9/15
 * Explain:接口处理工厂
 */
public class EnhanceRepositoryFactoryBean<JR extends JpaRepository<T, ID>, T, ID extends Serializable>
        extends JpaRepositoryFactoryBean<JR, T, ID> {
    public EnhanceRepositoryFactoryBean(Class<? extends JR> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
        return new EnhanceRepositoryFactory(entityManager);
    }

    private static class EnhanceRepositoryFactory<T, ID extends Serializable> extends JpaRepositoryFactory {
        private final EntityManager entityManager;

        public EnhanceRepositoryFactory(EntityManager entityManager) {
            super(entityManager);
            this.entityManager = entityManager;
        }

        @Override
        protected Object getTargetRepository(RepositoryInformation information) {
            return new EnhanceRepositoryImpl<T, ID>((Class<T>) information.getDomainType(), entityManager);
        }

        @Override
        protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
            return EnhanceRepositoryImpl.class;
        }
    }
}
