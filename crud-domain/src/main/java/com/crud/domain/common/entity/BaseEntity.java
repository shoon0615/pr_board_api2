/**
 * packageName  : com.crud.domain.common.entity
 * fileName     : BaseEntity
 * author       : SangHoon
 * date         : 2025-01-07
 * description  : Id + 공통 컬럼 Listener
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-01-07          SangHoon             최초 생성
 */
package com.crud.domain.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
//@SoftDelete
public abstract class BaseEntity extends BaseMemberEntity {

    /** 고유 ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("고유 ID")
    protected Long id;

    /*@Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;*/

    /** 삭제 여부 */
    @Comment("삭제 여부")
    @Column(updatable = false)
    protected LocalDateTime deletedAt;

}
