/**
 * packageName  : com.crud.api.config.security
 * fileName     : SpringSecurityConfig
 * author       : SangHoon
 * date         : 2025-01-10
 * description  : SpringSecurity 보안 및 접근(인증 권한) 체크
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-01-10          SangHoon             최초 생성
 */
package com.crud.api.config.security;

import com.crud.api.common.enumeration.AuthConstants;
import com.crud.api.common.util.JwtUtil;
import com.crud.api.config.handler.filter.JwtAuthenticationFilter;
import com.crud.api.config.handler.filter.LoginFilter;
import com.crud.domain.domains.enumeration.MemberRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
//@EnableMethodSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final HandlerExceptionResolver handlerExceptionResolver;

    /**
     * SpringSecurity 기본 설정
     * @method       : filterChain
     * @author       : SangHoon
     * @date         : 2025-04-05 PM 3:31
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
//                .cors(cors -> cors.configurationSource(this.corsConfigurationSource()))
                .headers(headersConfigurer -> headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .authorizeHttpRequests(request -> request
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers(AuthConstants.PATTERNS.PERMIT_ALL.getArray()).permitAll()
                        .requestMatchers(AuthConstants.PATTERNS.ADMIN.getArray()).hasRole(MemberRole.ROLE_ADMIN.getRole())
                        .requestMatchers(AuthConstants.PATTERNS.USER.getArray()).hasRole(MemberRole.ROLE_USER.getRole())
                        .anyRequest().authenticated()
                )
                .sessionManagement(SessionConfigurer -> SessionConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(Customizer.withDefaults())
                .addFilterBefore(this.jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(this.loginFilter(), JwtAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(this.authenticationEntryPoint())
                        .accessDeniedHandler(this.accessDeniedHandler())
                )
        ;
        return http.build();
    }

    /**
     * 로컬 내 CORS 허용
     * @method       : corsConfigurationSource
     * @author       : SangHoon
     * @date         : 2025-04-05 PM 3:30
     */
//    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", this.corsConfiguration());
        return source;
    }

    private CorsConfiguration corsConfiguration() {
        Long maxAge = 3600L;

        CorsConfiguration config = new CorsConfiguration();
//        config.applyPermitDefaultValues();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedMethod("*");
//        config.addAllowedHeader("*");
//        config.addExposedHeader("*");
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("*"));
        config.setMaxAge(maxAge);

        return config;
    }

    /**
     * filter 에 적합한 AuthenticationManager 를 호출
     * @method       : authenticationManager
     * @author       : SangHoon
     * @date         : 2025-04-05 PM 3:31
     */
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return this.authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * AuthenticationProvider 에 사용될 PasswordEncoder 등록
     * @method       : passwordEncoder
     * @author       : SangHoon
     * @date         : 2025-04-05 PM 3:31
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * JWT Token 로그인 필터
     * @method       : loginFilter
     * @author       : SangHoon
     * @date         : 2025-04-09 AM 5:37
     */
    public LoginFilter loginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter(this.authenticationManager(), this.jwtUtil);
        loginFilter.setFilterProcessesUrl(AuthConstants.PATTERNS.LOGIN.getList().get(0));
        return loginFilter;
    }

    /**
     * JWT Token 검증 필터
     * @method       : jwtAuthenticationFilter
     * @author       : SangHoon
     * @date         : 2025-04-09 AM 5:37
     */
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return new JwtAuthenticationFilter(this.jwtUtil);
    }

    /**
     * Spring Security Filter 인증 실패 Exception 처리(401: Unauthorized)
     * @method       : loginFilter
     * @author       : SangHoon
     * @date         : 2025-04-09 AM 5:37
     */
    private AuthenticationEntryPoint authenticationEntryPoint() {
        var exception = new AuthenticationServiceException("자격 증명에 실패하였습니다.");
        return (request, response, authException) -> {
            log.error("AuthenticationException: {}", authException.getMessage());
            this.handlerExceptionResolver.resolveException(request, response, null, exception);
        };
    }

    /**
     * Spring Security Filter 인가 실패 Exception 처리(403: Forbidden)
     * @method       : loginFilter
     * @author       : SangHoon
     * @date         : 2025-04-09 AM 5:37
     */
    private AccessDeniedHandler accessDeniedHandler() {
        var exception = new AccessDeniedException("접근 권한이 없습니다.");
        return (request, response, accessDeniedException) -> {
            log.error("AccessDeniedException: {}", accessDeniedException.getMessage());
            this.handlerExceptionResolver.resolveException(request, response, null, exception);
        };
    }

}
