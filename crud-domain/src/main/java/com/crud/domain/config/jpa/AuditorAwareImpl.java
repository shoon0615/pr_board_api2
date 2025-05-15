/**
 * packageName  : com.crud.domain.config.jpa
 * fileName     : AuditorAwareImpl
 * author       : SangHoon
 * date         : 2025-05-13
 * description  : DB 의 등록자/수정자 자동 처리(로그인 정보에서 ID 추출)
 *                  실무에서는 세션 정보나 스프링 시큐리티 로그인 정보에서 ID를 받음
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-05-13          SangHoon             최초 생성
 */
package com.crud.domain.config.jpa;

import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;

import java.util.Optional;
import java.util.UUID;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {
        /*Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(null == authentication || !authentication.isAuthenticated()) {
//            return null;
            return Optional.empty();
        }

        // 사용자 환경에 맞게 로그인한 사용자의 정보를 불러온다.
        UserDetailsEntity userDetails = (UserDetailsEntity) authentication.getPrincipal();

        return Optional.of(userDetails.getMbId());*/

        return Optional.of(UUID.randomUUID().toString());   // (임시) 랜덤값
    }

}
