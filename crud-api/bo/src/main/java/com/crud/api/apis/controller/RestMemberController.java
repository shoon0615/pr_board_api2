/**
 * packageName  : com.crud.api.apis.controller
 * fileName     : RestMemberController
 * author       : SangHoon
 * date         : 2025-04-05
 * description  : 회원 관련 RestController
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-04-05          SangHoon             최초 생성
 */
package com.crud.api.apis.controller;

import com.crud.api.apis.schema.RestMemberControllerDocs;
import com.crud.biz.service.MemberService;
import com.crud.common.dto.response.ApiResponse;
import com.crud.domain.domains.dto.request.LoginRequest;
import com.crud.domain.domains.dto.request.MemberRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class RestMemberController implements RestMemberControllerDocs {

    private final MemberService loginService;

    /**
     * 로그인
     * @method       : login
     * @author       : SangHoon
     * @date         : 2025-04-05 AM 4:23
     */
    @Override
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody final LoginRequest request) {
        return ApiResponse.ok(loginService.login(request));
    }

    /**
     * 회원가입
     * @method       : signup
     * @author       : SangHoon
     * @date         : 2025-04-05 AM 4:23
     */
    @Override
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody final MemberRequest request) {
        loginService.signup(request);
        return ApiResponse.ok();
    }

}
