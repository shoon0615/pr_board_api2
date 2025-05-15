/**
 * packageName  : com.crud.domain.domains.entity.member
 * fileName     : MemberEntity
 * author       : SangHoon
 * date         : 2025-04-05
 * description  : Member(회원) 테이블 Entity
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-04-05          SangHoon             최초 생성
 */
package com.crud.domain.domains.entity.member;

import com.crud.domain.common.entity.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString(callSuper = true)
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Comment("회원")
public class MemberEntity extends BaseTimeEntity {

    @Embedded
    private EmbMemberInfo memberInfo = new EmbMemberInfo();

    @Id
    @Comment("회원 ID")
    private String id;

    @Comment("비밀번호")
    @Column(nullable = false)
    private String password;

    @Comment("권한 ID")
    @OneToMany(mappedBy = "member")
    private List<GrantedAuthorityEntity> authorities = new ArrayList<>();

    @JsonIgnore
    @Comment("계정 만료 여부")
    @ColumnDefault("true")
    private boolean accountNonExpired = true;

    @JsonIgnore
    @Comment("계정 잠금 여부")
    @ColumnDefault("true")
    private boolean accountNonLocked = true;

    @JsonIgnore
    @Comment("자격 증명(비밀번호) 만료 여부")
    @ColumnDefault("true")
    private boolean credentialsNonExpired = true;

    @JsonIgnore
    @Comment("계정 활성화(사용) 여부")
    @ColumnDefault("true")
    private boolean enabled = true;

    /** UserDetails 의 @Override 용도 */
    public List<SimpleGrantedAuthority> getAuthorities() {
        return this.authorities.stream().map(authority -> new SimpleGrantedAuthority(authority.getAuthority())).toList();
    }

    /** 비밀번호 암호화 */
    public MemberEntity encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
        return this;
    }

}