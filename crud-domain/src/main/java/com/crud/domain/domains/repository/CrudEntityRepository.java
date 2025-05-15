/**
 * packageName  : com.crud.domain.domains.repository
 * fileName     : CrudEntityRepository
 * author       : SangHoon
 * date         : 2025-01-07
 * description  : CRUD 테이블 관련 DB 작업(JPQL)
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-01-08          SangHoon             최초 생성
 */
package com.crud.domain.domains.repository;

import com.crud.domain.common.repository.JpaRepositoryExtension;
import com.crud.domain.domains.entity.CrudEntity;
import com.crud.domain.domains.repository.querydsl.CrudEntityRepositoryCustom;

import java.util.Optional;

public interface CrudEntityRepository extends JpaRepositoryExtension<CrudEntity, Long>, CrudEntityRepositoryCustom {

    // 두개가 동일한 기능 수행
    /** TOP 1 조회(id 내림차순) */
    CrudEntity findFirstByOrderByIdDesc();

    /** TOP 1 조회(id 내림차순) */
    Optional<CrudEntity> findTop1ByOrderByIdDesc();

}
