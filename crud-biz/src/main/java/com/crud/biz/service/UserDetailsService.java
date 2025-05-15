/**
 * packageName  : com.crud.biz.service
 * fileName     : UserDetailsService
 * author       : SangHoon
 * date         : 2025-03-26
 * description  : 인증 관련 ServiceImpl(구현체)
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-03-26          SangHoon             최초 생성
 */
package com.crud.biz.service;

import com.crud.domain.domains.entity.member.MemberEntity;
import com.crud.domain.domains.entity.member.UserDetailsEntity;
import com.crud.domain.domains.repository.MemberEntityRepository;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final MemberEntityRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!StringUtils.hasText(username)) {
            throw new AuthenticationServiceException("회원 ID가 비어있습니다.");
        }

        // QueryDSL 을 통해서 UserDetailsEntity 로 반환 예정
        MemberEntity member = memberRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("회원을 찾을 수 없습니다."));

        // DTO 변환(entity -> userDetails)
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        UserDetailsEntity userDetails = mapper.convertValue(member, new TypeReference<>() {});

        return userDetails;
    }

}