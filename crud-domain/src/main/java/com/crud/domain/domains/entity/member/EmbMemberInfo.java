/**
 * packageName  : com.crud.domain.domains.entity.member
 * fileName     : EmbMemberInfo
 * author       : SangHoon
 * date         : 2025-04-05
 * description  : Member(회원) 테이블 Entity 상세 정보
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-04-05          SangHoon             최초 생성
 */
package com.crud.domain.domains.entity.member;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmbMemberInfo {

    @Comment("이름")
    @Column(length = 50)
    private String name;

    @Comment("생년월일")
//    @Temporal(TemporalType.DATE)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd", timezone = "Asia/Seoul")
    private LocalDate birthDate;

    @Comment("성별")
//    @Column(columnDefinition = "CHECK (gender IN ('M', 'W')")
//    @Check(constraints = "gender IN ('M', 'W')")
//    @Enumerated(EnumType.STRING)
    private String gender;

    @Comment("이메일")
    @Column(length = 50)
    private String email;

    @Comment("연락처")
//    @Column(length = 11)
//    @Column(name = "tel_no", nullable = false, length = 11, unique = true, updatable = false)
    private String telNo;

    @Comment("우편번호")
    private String zipCode;

    @Comment("기본 주소")
    @Column(length = 2000)
    private String address;

    @Comment("상세 주소")
    @Column(length = 4000)
    private String address2;

    /*@JsonIgnoreProperties("member")
    @OneToMany(mappedBy = "member")
    private List<BoardEntity> board = new ArrayList<>();*/

}