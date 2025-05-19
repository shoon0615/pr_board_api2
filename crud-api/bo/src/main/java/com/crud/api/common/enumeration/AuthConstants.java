/**
 * packageName  : com.crud.api.common.enumeration
 * fileName     : AuthConstants
 * author       : SangHoon
 * date         : 2025-04-05
 * description  : 인증 관련 URL 담당
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-04-05          SangHoon             최초 생성
 */
package com.crud.api.common.enumeration;

import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@UtilityClass
public class AuthConstants {

    public final String AUTH_ACCESS_HEADER = "Authorization";
    public final String AUTH_REFRESH_HEADER = "x-refresh-token";     // "Authorization-refresh"
    public final String TOKEN_TYPE = "Bearer";

    // Mapping Method 형식(GET/POST..) 및 세부 설정 추가 필요
//    @Getter
    @RequiredArgsConstructor
    public enum PATTERNS {
        LOGIN(() -> List.of(
                "/api/v1/login", "/api/v1/signup"
        ))
        , USER(() -> List.of(
                "/api/v1/user/**"
        ))
        , ADMIN(() -> List.of(
                "/api/v1/admin/**"
        ))
        , PERMIT_ALL(() -> {
            // Arrays.asList: add 가능, null 가능
            // List.of: 불변(final), add 불가능, null 불가능
            List<String> list = List.of(
                    "/v3/api-docs/**"
                    , "/configuration/**"
                    , "/swagger*/**"
                    , "/webjars/**"
                    , "/swagger-ui/**"
                    , "/docs"
//                    , "/error"
                    , "/api/v1/crud/**"
                    , "/api/v1/board/**"
            );

            List<String> combinedList = new ArrayList<>(LOGIN.getList());
            combinedList.addAll(list);
            return combinedList;
        })
        ;

        private final Supplier<List<String>> supplier;

        public List<String> getList() {
            return supplier.get();
        }

        public String[] getArray() {
            return supplier.get().toArray(new String[0]);
        }
    }

    /* 추후 CORS 적용하며 확인 및 변경(Enum?) 예정 */

    /** CORS - 제외될 HTTP Url */
    public final List<String> CORS_URL_PATTERNS = List.of(
            "http://localhost:8090"
    );

    /** CORS - 제외될 HTTP Method */
    public final List<String> CORS_METHOD_PATTERNS = List.of(
            HttpMethod.GET.name()
            , HttpMethod.POST.name()
            , HttpMethod.PUT.name()
            , HttpMethod.PATCH.name()
            , HttpMethod.DELETE.name()
            , HttpMethod.OPTIONS.name()
    );

    /** CORS - 제외될 HTTP Header */
    public final List<String> CORS_HEADERS_PATTERNS = List.of(
            "Authorization"
            , "Authorization-refresh"
            , "token"
            , "Cache-Control"
            , "Content-Type"
            , "Accept"
            , "X-Requested-With"
            , "X-XSRF-token"
    );

    /** CORS - 제외될 HTTP Response Header */
    public final List<String> CORS_EXPOSED_HEADERS_PATTERNS = List.of(
            "Authorization"
            , "Authorization-refresh"
    );

}
