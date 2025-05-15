/**
 * packageName  : com.crud.domain.config.jpa
 * fileName     : JpaConfig
 * author       : SangHoon
 * date         : 2025-01-09
 * description  : JPA 의 환경설정 구성
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-01-09          SangHoon             최초 생성
 */
package com.crud.domain.config.jpa;

import com.crud.domain.DomainPackageLocation;
import com.crud.domain.domains.repository.JpaPackageLocation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaAuditing
@EntityScan(basePackageClasses = DomainPackageLocation.class)
@EnableJpaRepositories(basePackageClasses = JpaPackageLocation.class)
public class JpaConfig {

    // DB 사용을 위한 주요 인터페이스 주입(@Autowired 안됨)
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * JPAQueryFactory 를 bean 으로 구성해 EntityManager 선언을 생략 가능
     * @method       : jpaQueryFactory
     * @author       : SangHoon
     * @date         : 2025-01-09 AM 1:26
     */
    @Bean
//    @ConditionalOnMissingBean       // Bean 등록 중복 방지
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }

    /**
     * DB 의 등록자/수정자 자동 처리
     * @method       : auditorProvider
     * @author       : SangHoon
     * @date         : 2025-01-09 AM 1:26
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }

}

