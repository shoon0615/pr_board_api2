/**
 * packageName  : com.crud.domain.domains.entity.board
 * fileName     : BoardLikeEntity
 * author       : SangHoon
 * date         : 2025-05-18
 * description  : 게시판 좋아요 Entity
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-05-18          SangHoon             최초 생성
 */
package com.crud.domain.domains.entity.board;

import com.crud.domain.common.entity.BaseEntity;
import com.crud.domain.domains.entity.member.MemberEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"boardId", "memberId"}))    // hibernate 에서 자동 매핑은 해주지만, 명시적 지정 시 문제될수 있어 원본 그대로 생성 권장
//@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"board_id", "member_id", "delete_yn"}))  // 당연히 @MapsId 없어도 가능(default: 연관관계 column + "_id") -> delete_yn 이 없으면 적용 불가
@SQLDelete(sql = "UPDATE board_like_entity SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@Comment("게시판 좋아요")
public class BoardLikeEntity extends BaseEntity {

    /** @MapsId 적용 */
    // bdId
    @Column(name="board_id")
    private Long boardId;

    @Comment("게시판 테이블")
    @JsonIgnoreProperties("like")
    @MapsId("boardId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private BoardEntity board;

    @Column(name="member_id")
    private String memberId;

    @Comment("회원 테이블")
    @JsonIgnoreProperties("like")
    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    /** @MapsId 미적용 */
    /*@Comment("게시판 테이블")
    @JsonIgnoreProperties("like")
    @ManyToOne(fetch = FetchType.LAZY)
    private BoardEntity board;

    @Comment("회원 테이블")
    @JsonIgnoreProperties("like")
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;*/

}
