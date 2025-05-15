/**
 * packageName  : com.crud.domain.domains.enumeration
 * fileName     : MemberRole
 * author       : SangHoon
 * date         : 2025-04-05
 * description  : Authentication 의 authorities(권한) 담당
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-04-05          SangHoon             최초 생성
 */
package com.crud.domain.domains.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {

    ROLE_ADMIN("ADMIN", "관리자")
    , ROLE_USER("USER", "유저")
    ;

    private final String role;
    private final String name;

    /*public String getRoleName() {
        return this.name.replace("ROLE_", "");
    }*/

}