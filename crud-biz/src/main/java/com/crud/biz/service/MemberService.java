/**
 * packageName  : com.crud.biz.service
 * fileName     : MemberService
 * author       : SangHoon
 * date         : 2025-04-05
 * description  : 회원 관련 ServiceImpl(구현체)
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-03-29          SangHoon             최초 생성
 */
package com.crud.biz.service;

import com.crud.biz.useCase.MemberUseCase;
import com.crud.common.enumeration.response.ResponseCode;
import com.crud.common.exception.BusinessException;
import com.crud.common.util.ObjectMapperUtil;
import com.crud.domain.domains.dto.request.LoginRequest;
import com.crud.domain.domains.dto.request.MemberRequest;
import com.crud.domain.domains.entity.member.GrantedAuthorityEntity;
import com.crud.domain.domains.entity.member.MemberEntity;
import com.crud.domain.domains.enumeration.MemberRole;
import com.crud.domain.domains.repository.MemberAuthorityRepository;
import com.crud.domain.domains.repository.MemberEntityRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService implements MemberUseCase {

    private final MemberEntityRepository memberRepository;
    private final MemberAuthorityRepository authorityRepository;

    /*private final BCryptPasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public Map<String, Object> login(final LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(request.getId(), request.getPassword()));

        String accessToken = jwtUtil.createAccessToken(authentication);
        String refreshToken = jwtUtil.createRefreshToken(authentication);

        Map<String, Object> result = Map.of(
            "token", accessToken
                , "refresh-token", refreshToken
        );

        return result;
    }*/

    @Override
    public Map<String, Object> login(final LoginRequest request) {
        Map<String, Object> result = Map.of(
                "token", "test"
                , "refresh-token", "test"
        );

        return result;
    }

    @Override
    @Transactional
    public void signup(final MemberRequest request) {
        try {
            if (memberRepository.existsById(request.getId())) {
                throw new BusinessException("이미 존재하는 ID입니다.");
            }

            // TODO: QueryDSL 로 변경
            MemberEntity params = ObjectMapperUtil.convertType(request, new TypeReference<>() {});
//            params.encodePassword(passwordEncoder);

            MemberEntity member = memberRepository.save(params);

            // TODO: 임시 회원 권한 셋팅
//            authorityRepository.save(GrantedAuthorityEntity.builder().role(MemberRole.ROLE_ADMIN).member(member).build());
            authorityRepository.save(new GrantedAuthorityEntity(MemberRole.ROLE_ADMIN, member));
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ResponseCode.ErrorCode.SAVE_ERROR);
        }
    }

}
