/**
 * packageName  : com.crud.domain.domains.repository
 * fileName     : BoardLikeEntityRepository
 * author       : SangHoon
 * date         : 2025-05-19
 * description  : BoardLike 테이블 관련 DB 작업(JPQL)
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-05-19          SangHoon             최초 생성
 */
package com.crud.domain.domains.repository;

import com.crud.domain.common.repository.JpaRepositoryExtension;
import com.crud.domain.domains.entity.board.BoardLikeEntity;

import java.util.Optional;

public interface BoardLikeEntityRepository extends JpaRepositoryExtension<BoardLikeEntity, Long> {

    /** 좋아요 표시 여부 확인 -> 모두 같은 의도의 기능 */

    // @Query(JPQL) 대신 Named Query 이용 -> @Query(FETCH) 진행 방식이 아니기에, 이후 연관관계 조회하면 추가 SQL 발생
//    BoardLikeEntity findFirstByBoardIdAndMemberId(Long boardId);
    Optional<BoardLikeEntity> findByBoardIdAndMemberId(Long boardId, String memberId);  // @MapsId 사용 시, join 미진행
//    BoardLikeEntity findByBoard_IdAndMember_Id(Long boardId, String memberId);          // @MapsId 미사용 시, 기존 메서드 이름 기반 JPQL 방식(Named Query)

    // SELECT COUNT(bl.id) FROM BoardLikeEntity bl WHERE bl.board.id = :id and bl.member.id='test';
//    @Query("SELECT COUNT(bl) FROM BoardLikeEntity bl JOIN bl.board b WHERE bl.id = :id AND bl.member.id = :memberId")
    long countByBoardIdAndMemberId(Long boardId, String memberId);      // @MapsId 사용

    // SELECT COUNT(bl.id) FROM BoardLikeEntity bl LEFT JOIN bl.board b LEFT JOIN bl.m m1_0 WHERE bl.id = :id AND m.id = 'test';
    long countByBoard_IdAndMember_Id(Long boardId, String memberId);    // @MapsId 미사용

    // SELECT COUNT(bl.id) FROM BoardLikeEntity bl WHERE bl.board.id = :id AND bl.member.id = 'test' FETCH FIRST 1 ROWS ONLY;
    boolean existsByBoardIdAndMemberId(Long boardId, String memberId);

}
