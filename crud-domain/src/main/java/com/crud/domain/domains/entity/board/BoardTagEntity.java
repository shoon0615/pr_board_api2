/**
 * packageName  : com.crud.domain.domains.entity.board
 * fileName     : BoardTagEntity
 * author       : SangHoon
 * date         : 2025-05-18
 * description  : 게시판 태그 Entity
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-05-18          SangHoon             최초 생성
 */
package com.crud.domain.domains.entity.board;

import com.crud.domain.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE board_tag_entity SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@Comment("게시판 태그")
public class BoardTagEntity extends BaseEntity {

    /** @MapsId 적용 -> 복합키 쓰는 경우가 아니면 굳이 안써도 될 듯?? */
    /*@Column(name="board_id")
    private Long boardId;

    @Setter(AccessLevel.PROTECTED)
    @Comment("게시판 테이블")
    @JsonIgnoreProperties("tag")
    @MapsId("boardId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private BoardEntity board;

    @Deprecated
    public void addBoard(final BoardEntity board) {
        this.board = board;             // 연관관계의 주인 설정 -> 불필요하지만 연관 무결성을 위해 작성
        this.boardId = board.getId();   // 실질적으론 @MapsId 만 존재해도 fk 생성됨
    }*/

    /** @MapsId 미적용 */
    @Setter(AccessLevel.PROTECTED)
    @Comment("게시판 테이블")
    @JsonIgnoreProperties("tag")
    @ManyToOne(fetch = FetchType.LAZY)
    private BoardEntity board;

    @Comment("내용")
    @Column(length = 20, nullable = false)
    private String contents;

}
