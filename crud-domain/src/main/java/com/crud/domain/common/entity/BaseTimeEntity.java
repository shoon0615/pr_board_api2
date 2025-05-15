/**
 * packageName  : com.crud.domain.common.entity
 * fileName     : BaseTimeEntity
 * author       : SangHoon
 * date         : 2025-01-07
 * description  : 공통 Date 컬럼 Listener
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-01-07          SangHoon             최초 생성
 */
package com.crud.domain.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    /** 생성일자 */
    @CreatedDate
    @Comment("생성 일자")
//    @Column(nullable = false, updatable = false)
    @Column(updatable = false)
    protected LocalDateTime createdDate;

    /** 수정일자 */
    @LastModifiedDate
    @Comment("수정 일자")
    protected LocalDateTime modifiedDate;

    /** 상속(extends) 받는 Entity 객체의 컬럼들까지 모두 출력 */
    /*@Override
    public String toString() {
        try {
            return ObjectMapperUtil.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return new ReflectionToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
//                .setExcludeFieldNames("member")
                .toString();
        }
    }*/

}
