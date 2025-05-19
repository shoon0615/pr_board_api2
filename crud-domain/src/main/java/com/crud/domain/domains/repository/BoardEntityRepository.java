/**
 * packageName  : com.crud.domain.domains.repository
 * fileName     : BoardEntityRepository
 * author       : SangHoon
 * date         : 2025-05-17
 * description  : Board 테이블 관련 DB 작업(JPQL)
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-05-17          SangHoon             최초 생성
 */
package com.crud.domain.domains.repository;

import com.crud.domain.common.repository.JpaRepositoryExtension;
import com.crud.domain.domains.entity.board.BoardEntity;
import com.crud.domain.domains.repository.querydsl.BoardEntityRepositoryCustom;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardEntityRepository extends JpaRepositoryExtension<BoardEntity, Long>, BoardEntityRepositoryCustom {

    /** JPQL 예제 */
    // 하기 3개 쿼리 모두 같은 내용
    BoardEntity findByTitle(String title);      // 메소드 이름 기반 JPQL(Name Query)

    @Query("SELECT b FROM BoardEntity b WHERE b.title = :title")
    BoardEntity findByTitle2(String title);     // @Query(JPQL) -> 사용할 변수명이 같은 경우, @Param 생략 가능

    @Query("SELECT b FROM BoardEntity b WHERE b.title = :title")
    BoardEntity findByTitle3(@Param("title") String title);     // @Query(JPQL) 정석

    /** Named Query(Join) 예제 -> @ManyToOne, @OneToMany 상관없이 메인에 left join 으로 조회 */
    // SELECT b FROM BoardEntity b LEFT JOIN b.member m WHERE m.id = :memberId       // (실제) JPQL 에서 JOIN 시, 자동으로 @Id 와 연관관계 @Id 가 ON 으로 매핑 -> 다만, 문법에선 생략
    @Deprecated     // memberId 가 아닌 boardId 로 찾아야함
    BoardEntity findByMember_Id(String memberId);

//    @Deprecated
//    BoardEntity findByIdMember(Long id);    // 에러 발생 -> 불가능한 예제(객체 대신 컬럼 필요)

    // SELECT b FROM BoardEntity b LEFT JOIN b.member m WHERE b.id = :id AND m.id = :memberId
    @Deprecated
    BoardEntity findByIdAndMember_Id(Long id, String memberId);

    /** JPQL(Join) 예제 */
    // SELECT b FROM BoardEntity b JOIN b.like m WHERE b.id = :id
    @Deprecated
    @Query("SELECT DISTINCT b FROM BoardEntity b JOIN b.like bl WHERE b.id = :id")
    BoardEntity findById_BoardLike(@Param("id") Long id);

    // SELECT b, bl FROM BoardEntity b JOIN b.like m WHERE b.id = :id AND b.member.id = 'test'
    @Deprecated
    @Query("SELECT b FROM BoardEntity b JOIN FETCH b.like bl WHERE b.id = :id AND b.member.id = 'test'")
    BoardEntity findById_BoardLikeAndMember_IdTest(@Param("id") Long id);   // FETCH 미사용 시 board 만, 사용 시 board, boardLike 함께 조회 -> 이후 boardLike 조회해도 추가 SQL 미발생

    /** 좋아요 표시 여부 확인 */
    @Query("SELECT b FROM BoardEntity b JOIN b.like bl WHERE b.id = :id AND b.member.id = :memberId")
    Optional<BoardEntity> findById_BoardLikeAndMember_Id(@Param("id") Long id, @Param("memberId") String memberId);

}
