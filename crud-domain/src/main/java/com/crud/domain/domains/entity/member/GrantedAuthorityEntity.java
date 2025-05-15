/**
 * packageName  : com.crud.domain.domains.entity.member
 * fileName     : GrantedAuthorityEntity
 * author       : SangHoon
 * date         : 2025-04-05
 * description  : MemberAuthority(회원별 권한) 테이블 Entity
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-04-05          SangHoon             최초 생성
 */
package com.crud.domain.domains.entity.member;

import com.crud.domain.domains.enumeration.MemberRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.springframework.security.core.GrantedAuthority;

@Getter
@ToString(callSuper = true)
@Entity
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Comment("회원별 권한")
public class GrantedAuthorityEntity implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("권한 ID")
    private Long id;

    @Comment("권한")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Comment("회원 ID")
//    @OneToOne(fetch = FetchType.LAZY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")     // 일치하는 명칭을 찾는게 아니라 custom 으로 생성할 명칭
    private MemberEntity member;

    @Override
    @JsonIgnore
    public String getAuthority() {
        return this.role.name();
    }

    // TODO: 임시 회원 권한 셋팅
    @Deprecated
    @Builder
    public GrantedAuthorityEntity(MemberRole role, MemberEntity member) {
        this.role = role;
        this.member = member;
    }

}