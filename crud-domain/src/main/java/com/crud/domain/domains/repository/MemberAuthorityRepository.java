/**
 * packageName  : com.crud.domain.domains.repository
 * fileName     : MemberAuthorityRepository
 * author       : SangHoon
 * date         : 2025-04-05
 * description  : 회원 권한 관련 DB 작업
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-04-05          SangHoon             최초 생성
 */
package com.crud.domain.domains.repository;

import com.crud.domain.common.repository.JpaRepositoryExtension;
import com.crud.domain.domains.entity.member.GrantedAuthorityEntity;

public interface MemberAuthorityRepository extends JpaRepositoryExtension<GrantedAuthorityEntity, Long> {
}
