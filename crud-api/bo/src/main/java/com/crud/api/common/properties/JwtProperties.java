/**
 * packageName  : com.crud.api.common.properties
 * fileName     : JwtProperties
 * author       : SangHoon
 * date         : 2025-04-05
 * description  : yml 데이터 수신(jwt)
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-04-05          SangHoon             최초 생성
 */
package com.crud.api.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @param secret JWT Secret 키
 * @param tokenValidityInSeconds AccessToken 만료 시간
 * @param refreshTokenValidityInSeconds RefreshToken 만료 시간
 */
@ConfigurationProperties("jwt")
public record JwtProperties(
        String secret
        , long tokenValidityInSeconds
        , long refreshTokenValidityInSeconds
) {

}