/**
 * packageName  : com.crud.api.config.handler.filter
 * fileName     : LoginFilter
 * author       : SangHoon
 * date         : 2025-04-05
 * description  : JWT Token 로그인 방식에 특화된 필터
 *                  (기존) formLogin + Session 로그인 방식에 특화된 필터
 *                  기존 로직 방식에 Token 을 발급하도록 설정(인증 정보(SecurityContext) 는 로그인 시 저장하지 않음)
 *                  해당 Filter 가 완료되면 JWT Token 검증 필터(JwtAuthenticationFilter) 와 내부 Security Filter 를 포함한 모든 Filter 미진행
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-04-05          SangHoon             최초 생성
 */
package com.crud.api.config.handler.filter;

import com.crud.api.common.enumeration.AuthConstants;
import com.crud.api.common.util.JwtUtil;
import com.crud.common.dto.response.ApiResponse;
import com.crud.common.util.ObjectMapperUtil;
import com.crud.domain.domains.dto.request.LoginRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Deprecated
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        super.setAuthenticationManager(authenticationManager);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authentication;

        try {
            LoginRequest loginRequest = ObjectMapperUtil.deserialization(String.valueOf(request.getInputStream()), LoginRequest.class);
            authentication = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getId(), loginRequest.getPassword());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return this.getAuthenticationManager().authenticate(authentication);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws ServletException, IOException {
        String token = jwtUtil.createAccessToken(authResult);

        response.addHeader(AuthConstants.AUTH_ACCESS_HEADER, String.format("${%s} ${%s}", AuthConstants.TOKEN_TYPE, token));
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> resultData = Map.of(
                "token", token
                , "userDetails", authResult.getPrincipal()
        );
        ResponseEntity resultMap = ApiResponse.ok(resultData);

        response.getWriter().write(ObjectMapperUtil.serialization(resultMap.getBody()));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        String failMsg = "";

        if (failed instanceof AuthenticationServiceException ||
                failed instanceof BadCredentialsException ||
                failed instanceof LockedException ||
                failed instanceof DisabledException ||
                failed instanceof AccountExpiredException ||
                failed instanceof CredentialsExpiredException) {
            failMsg = "로그인 정보가 일치하지 않습니다.";
        }

        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

//        throw new BusinessException(failMsg);
        ResponseEntity resultMap = ApiResponse.fail(failMsg);

        response.getWriter().write(ObjectMapperUtil.serialization(resultMap.getBody()));
    }

}
