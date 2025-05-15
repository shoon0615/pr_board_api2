/**
 * packageName  : com.crud.domain.config.jpa
 * fileName     : JpaConfigTest
 * author       : SangHoon
 * date         : 2025-05-14
 * description  : 테스트용 JPA 설정
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-05-14          SangHoon             최초 생성
 */
package com.crud.domain.config.jpa;

import com.crud.domain.DomainPackageLocation;
import com.crud.domain.domains.repository.JpaPackageLocation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@TestConfiguration
@EnableJpaAuditing
@EntityScan(basePackageClasses = DomainPackageLocation.class)
@EnableJpaRepositories(basePackageClasses = JpaPackageLocation.class)
public class JpaConfigTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    @ConditionalOnMissingBean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    @ConditionalOnMissingBean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }

}