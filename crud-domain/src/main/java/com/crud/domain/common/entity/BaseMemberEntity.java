/**
 * packageName  : com.crud.domain.common.entity
 * fileName     : BaseMemberEntity
 * author       : SangHoon
 * date         : 2025-05-12
 * description  : 공통 Member 컬럼 Listener
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-05-12          SangHoon             최초 생성
 */
package com.crud.domain.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseMemberEntity extends BaseTimeEntity {

    /** 생성자 */
    @CreatedBy
    @Comment("생성자")
    @Column(updatable = false)
    protected String createdBy;

    /** 수정자 */
    @LastModifiedBy
    @Comment("수정자")
    protected String modifiedBy;

}
