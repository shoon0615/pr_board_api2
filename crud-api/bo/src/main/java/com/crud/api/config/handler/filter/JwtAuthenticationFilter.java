/**
 * packageName  : com.crud.api.config.handler.filter
 * fileName     : JwtAuthenticationFilter
 * author       : SangHoon
 * date         : 2025-04-05
 * description  : JWT Token 검증 필터
 *                  Security Config 의 authorizeHttpRequests.requestMatchers 에서
 *                  인증 또는 권한이 필요한 URL 에 해당되는 경우, 인증되지 않으면 403 에러 발생
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-04-05          SangHoon             최초 생성
 */
package com.crud.api.config.handler.filter;

import com.crud.api.common.enumeration.AuthConstants;
import com.crud.api.common.util.JwtUtil;
import com.crud.common.util.RequestUriUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain) throws IOException, ServletException {
        String token = jwtUtil.resolveToken(request);

        if (token != null && jwtUtil.validateToken(token)) {
            Authentication authentication = jwtUtil.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

    /**
     * <pre>
     * 해당 요청 URL 은 위 Filter 미적용
     * 사용자 로그인 정보를 사용할 수 있어서 PERMIT_ALL -> LOGIN 으로 변경
     * PERMIT_ALL 로 진행할 경우, Filter 자체를 패스하여 SecurityContextHolder.getContext().setAuthentication(로그인 정보 생성) 미진행
     * </pre>
     */
    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        return RequestUriUtil.matchesAntPath(request, AuthConstants.PATTERNS.LOGIN.getList());
    }

}