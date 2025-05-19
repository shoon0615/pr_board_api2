/**
 * packageName  : com.crud.domain.domains.repository
 * fileName     : BoardTagEntityRepository
 * author       : SangHoon
 * date         : 2025-05-19
 * description  : BoardTag 테이블 관련 DB 작업(JPQL)
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-05-19          SangHoon             최초 생성
 */
package com.crud.domain.domains.repository;

import com.crud.domain.common.repository.JpaRepositoryExtension;
import com.crud.domain.domains.entity.board.BoardTagEntity;

public interface BoardTagEntityRepository extends JpaRepositoryExtension<BoardTagEntity, Long> {
}
