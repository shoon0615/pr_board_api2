/**
 * packageName  : com.crud.api.config.security
 * fileName     : SpringSecurityConfigTest
 * author       : SangHoon
 * date         : 2025-05-14
 * description  : 테스트용 SpringSecurity 설정
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-05-14          SangHoon             최초 생성
 */
package com.crud.api.config.security;

import com.crud.api.common.enumeration.AuthConstants;
import com.crud.domain.domains.enumeration.MemberRole;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
@EnableWebSecurity
public class SpringSecurityConfigTest {

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
        ;
        return http.build();
    }

}
