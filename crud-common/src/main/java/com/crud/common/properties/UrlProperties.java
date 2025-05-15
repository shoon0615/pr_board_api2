/**
 * packageName  : com.crud.common.properties
 * fileName     : UrlProperties
 * author       : SangHoon
 * date         : 2025-01-11
 * description  : yml 데이터 수신(url)
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-01-11          SangHoon             최초 생성
 */
package com.crud.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("url")
public record UrlProperties(BoProperties bo, FoProperties fo) {

    /*public record UrlProperties(BoProperties bo, FoProperties fo) {
        public record BoProperties(String apiUrl, String uiUrl) {}
        public record FoProperties(String apiUrl, String uiUrl) {}
    }*/

    public record BoProperties(String apiUrl) {}
    public record FoProperties(String apiUrl) {}

}
