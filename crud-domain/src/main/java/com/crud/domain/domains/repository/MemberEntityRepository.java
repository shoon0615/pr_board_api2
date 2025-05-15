/**
 * packageName  : com.crud.domain.domains.repository
 * fileName     : MemberRepository
 * author       : SangHoon
 * date         : 2025-04-05
 * description  : Member(회원) 테이블 관련 DB 작업(JPQL)
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-04-05          SangHoon             최초 생성
 */
package com.crud.domain.domains.repository;

import com.crud.domain.common.repository.JpaRepositoryExtension;
import com.crud.domain.domains.entity.member.MemberEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberEntityRepository extends JpaRepositoryExtension<MemberEntity, String> {

    /** 회원 조회(단건) */
    @Query("SELECT member FROM MemberEntity member JOIN FETCH member.authorities WHERE member.id=:id")
    Optional<MemberEntity> findById(String id);

    /** 회원 중복 체크 */
    boolean existsById(String id);

}
