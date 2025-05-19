/**
 * packageName  : com.crud.domain.domains.repository
 * fileName     : BoardReviewEntityRepository
 * author       : SangHoon
 * date         : 2025-05-20
 * description  : BoardReview 테이블 관련 DB 작업(JPQL)
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-05-20          SangHoon             최초 생성
 */
package com.crud.domain.domains.repository;

import com.crud.domain.common.repository.JpaRepositoryExtension;
import com.crud.domain.domains.entity.board.BoardReviewEntity;

import java.util.List;

public interface BoardReviewEntityRepository extends JpaRepositoryExtension<BoardReviewEntity, Long> {

//    @Query("SELECT br FROM BoardReviewEntity br WHERE br.board.id = :boardId ORDER BY br.createdAt ASC")
//    List<BoardReviewEntity> findByBoardIdOrderByCreatedAt(@Param("boardId") Long boardId);

    List<BoardReviewEntity> findByBoardId(Long boardId);

}
