/**
 * packageName  : com.crud.biz.useCase
 * fileName     : MemberUseCase
 * author       : SangHoon
 * date         : 2025-04-05
 * description  : 회원 관련 Service
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-04-05          SangHoon             최초 생성
 */
package com.crud.biz.useCase;

import com.crud.domain.domains.dto.request.LoginRequest;
import com.crud.domain.domains.dto.request.MemberRequest;

import java.util.Map;

public interface MemberUseCase {

    /** 로그인 */
    Map<String, Object> login(LoginRequest request);

    /** 회원가입 */
    void signup(MemberRequest request);

}
