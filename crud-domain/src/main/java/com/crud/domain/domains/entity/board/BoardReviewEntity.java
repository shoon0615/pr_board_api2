/**
 * packageName  : com.crud.domain.domains.entity.board
 * fileName     : BoardReviewEntity
 * author       : SangHoon
 * date         : 2025-05-18
 * description  : 게시판 리뷰 Entity
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-05-18          SangHoon             최초 생성
 */
package com.crud.domain.domains.entity.board;

import com.crud.domain.common.entity.BaseEntity;
import com.crud.domain.common.util.BeanMapperUtil;
import com.crud.domain.domains.dto.request.BoardRequest;
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
@SQLDelete(sql = "UPDATE board_review_entity SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@Comment("게시판 리뷰")
public class BoardReviewEntity extends BaseEntity {

    @Column(name="board_id")
    private Long boardId;

    @Comment("게시판 테이블")
    @JsonIgnoreProperties("review")
    @MapsId("boardId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private BoardEntity board;

    @Column(name="member_id")
    private String memberId;

    @Comment("회원 테이블")
    @JsonIgnoreProperties("review")
    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @Comment("내용")
//    @Column(length = 2000, nullable = false)
//    @Column(length = 2000)
    private String contents;

    /*@Comment("평점")
    @Check(constraints = "rating BETWEEN 1 AND 5 ")
    private int rating;*/

    /*@Comment("대댓글 레벨")
    @Check(constraints = "level BETWEEN 1 AND 5 ")
    private Integer level;*/

    public BoardReviewEntity update(BoardRequest.BoardReviewRequest request) {
//        this.contents = request.getContents();
        BeanMapperUtil.copyProperties(request, this);
        return this;
    }

}
