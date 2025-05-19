/**
 * packageName  : com.crud.domain.domains.entity.board
 * fileName     : BoardEntity
 * author       : SangHoon
 * date         : 2025-05-17
 * description  : 게시판 테이블 Entity
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-05-17          SangHoon             최초 생성
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
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE board_entity SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@Comment("게시판")
public class BoardEntity extends BaseEntity {

    @Comment("제목")
    @Column(length = 500, nullable = false)
    private String title;

    @Comment("내용")
//    @Column(columnDefinition = "TEXT", nullable = false)    // TEXT 형식(길이 제한 없음)
    @Column(nullable = false)
    private String contents;

    @Comment("조회 수")
    @ColumnDefault("0")
    private int hits = 0;

    @Comment("회원 테이블")
    @JsonIgnoreProperties("board")
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;

    @JsonIgnoreProperties("board")
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
//    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<BoardTagEntity> tag = new ArrayList<>();

    @JsonIgnoreProperties("board")
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardLikeEntity> like = new ArrayList<>();

    @JsonIgnoreProperties("board")
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardReviewEntity> review = new ArrayList<>();

    public BoardEntity update(BoardRequest request) {
        BeanMapperUtil.copyProperties(request, this);
        return this;
    }

    @Deprecated
    public void addMember(final MemberEntity member) {      // TODO: 테스트용 - 추후 삭제
        this.member = member;
    }

    public void addTag(final BoardTagEntity tag) {
        this.tag.add(tag);
        tag.setBoard(this);
//        tag.setBoardId(this.id);    // @MapsId 사용 시, CascadeType.PERSIST, CascadeType.MERGE 병행 불가능
    }

    public void addTags(final List<BoardTagEntity> tags) {
        tags.forEach(tag -> tag.setBoard(this));
        this.tag.addAll(tags);
    }

}
