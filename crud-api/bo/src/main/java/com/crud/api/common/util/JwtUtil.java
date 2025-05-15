/**
 * packageName  : com.crud.api.common.util
 * fileName     : JwtUtil
 * author       : SangHoon
 * date         : 2025-04-05
 * description  : JWT Token 관련 라이브러리
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-04-05          SangHoon             최초 생성
 */
package com.crud.api.common.util;

import com.crud.api.common.enumeration.AuthConstants;
import com.crud.api.common.properties.JwtProperties;
import com.crud.domain.domains.entity.member.UserDetailsEntity;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProperties jwtProperties;
    private SecretKey secretKey;

    private final UserDetailsService userDetailsService;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.secret().getBytes(StandardCharsets.UTF_8));
    }

    /** 인증 객체(userDetails) 를 전달받아 AccessToken 에 저장하여 전달 */
    public String createAccessToken(Authentication authentication) {
        return this.createToken(this.createClaims(authentication, jwtProperties.tokenValidityInSeconds()));
    }

    /** 인증 객체(userDetails) 를 전달받아 RefreshToken 에 저장하여 전달 */
    public String createRefreshToken(Authentication authentication) {
        return this.createToken(this.createClaims(authentication, jwtProperties.refreshTokenValidityInSeconds()));
    }

    /** JWT Token : Payload 에 생성할 정보 */
    private Claims createClaims(Authentication authentication, long validityInSeconds) {
        UserDetailsEntity userDetails = (UserDetailsEntity) authentication.getPrincipal();

        String jti = UUID.randomUUID().toString();
        Date now = new Date();
        Date expiration = new Date(now.getTime() + validityInSeconds * 1000);

        return Jwts.claims()
                .subject(userDetails.getUsername())     // 제목(ID)
                .id(jti)                                // 동시성 제어(동시 접속 시 구분 ID)
                .issuedAt(now)                          // 발행 시간
                .expiration(expiration)                 // 만료 시간
//                .add("userName", userDetails.getMemberInfo().getName())     // 추가 정보
                .build();
    }

    /** JWT Token : Header(암호 알고리즘) + Payload(내용), Signature(Secret) */
    private String createToken(Claims claims) {
        JwtBuilder builder = Jwts.builder()
                .claims(claims)
                .signWith(this.secretKey)
                ;
        return builder.compact();
    }

    // 추후 ExceptionHandler 또는 직접 ApiResponse 를 통해 ErrorMessage 출력되게 변경 예정
    public boolean validateToken(String token) {
        try {
            Claims claims = this.getClaims(token);
            log.info("expireTime :" + claims.getExpiration());
            log.info("userId :" + claims.get("userId"));
            log.info("userNm :" + claims.get("userNm"));
            return true;
        } catch (ExpiredJwtException exception) {
            log.debug("token expired " + token);
            log.error("Token Expired" + exception);
            return false;
        } catch (JwtException exception) {
            log.debug("token expired " + token);
            log.error("Token Tampered" + exception);
            return false;
        } catch (NullPointerException exception) {
            log.debug("token expired " + token);
            log.error("Token is null" + exception);
            return false;
        } catch (Exception e) {
            if (e instanceof SecurityException) {
                log.debug("[SecurityException] 잘못된 토큰");
                throw new JwtException("[SecurityException] 잘못된 토큰입니다.");
            } else if (e instanceof MalformedJwtException) {
                log.debug("[MalformedJwtException] 잘못된 토큰");
                throw new JwtException("[MalformedJwtException] 잘못된 토큰입니다.");
            } else if (e instanceof ExpiredJwtException) {
                log.debug("[ExpiredJwtException] 토큰 만료");
                throw new JwtException("[ExpiredJwtException] 토큰 만료");
            } else if (e instanceof UnsupportedJwtException) {
                log.debug("[UnsupportedJwtException] 잘못된 형식의 토큰");
                throw new JwtException("[UnsupportedJwtException] 잘못된 형식의 토큰");
            } else if (e instanceof SignatureException) {
                log.debug("[SignatureException] 허용되지 않은 토큰");
                throw new JwtException("[SignatureException] 허용되지 않은 토큰");
            } else if (e instanceof IllegalArgumentException) {
                log.debug("[IllegalArgumentException]");
                throw new JwtException("[IllegalArgumentException]");
            } else {
                log.debug("[토큰검증 오류]" + e.getClass());
                throw new JwtException("[토큰검증 오류] 미처리 토큰 오류");
            }
        }
    }

    /** JWT Token : Payload 에 저장된 정보 */
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(this.secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /** JWT Token 유효성 검증 */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AuthConstants.AUTH_ACCESS_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(AuthConstants.TOKEN_TYPE)) {
            return bearerToken.split(" ")[1];
        }
        return null;
    }

    /** Token 에 정보를 통해 인증 객체(userDetails) 생성 및 반환 */
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);

        UserDetailsEntity userDetails = (UserDetailsEntity) userDetailsService.loadUserByUsername(claims.getSubject());
        return UsernamePasswordAuthenticationToken.authenticated(userDetails, null, userDetails.getAuthorities());
    }

    /** 로그인 정보 호출 */
    public static UserDetailsEntity getLoginMember() {
        return (UserDetailsEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}