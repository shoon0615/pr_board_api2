/**
 * packageName  : com.crud.domain.domains.entity
 * fileName     : CrudEntity
 * author       : SangHoon
 * date         : 2025-01-07
 * description  : CRUD 테이블 Entity
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-01-07          SangHoon             최초 생성
 */
package com.crud.domain.domains.entity;

import com.crud.domain.common.entity.BaseEntity;
import com.crud.domain.common.util.BeanMapperUtil;
import com.crud.domain.domains.dto.request.CrudRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@ToString(callSuper = true)
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE crud_entity SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@Comment("테스트")
public class CrudEntity extends BaseEntity {

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

    /** 수정(Dirty Checking) */
    public CrudEntity update(CrudRequest request) {
        /*this.title = request.getTitle();
        this.contents = request.getContents();
        return this;*/

        // 객체 구조가 복잡/중첩된 경우 안될 수 있기에 확인 필요 -> 얕은 복사
//        BeanUtils.copyProperties(request, this);
        BeanMapperUtil.copyProperties(request, this);
        return this;
    }

}
