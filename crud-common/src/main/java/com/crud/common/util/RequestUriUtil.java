/**
 * packageName  : com.crud.common.util
 * fileName     : RequestUriUtil
 * author       : SangHoon
 * date         : 2025-04-05
 * description  : HttpServletRequest 관련 라이브러리
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-04-05          SangHoon             최초 생성
 */
package com.crud.common.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.AntPathMatcher;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RequestUriUtil {

    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    // @PostConstruct
    /*static {

    }*/

    /*public static boolean matchesRegex(HttpServletRequest request, String[] array) {
        // contains: 포함, startsWith: 시작, matches: 일치
        return Arrays.stream(array).anyMatch(request.getRequestURI()::matches);
    }*/

    /** 정규식(Regex) 패턴 매칭 비교  */
    // 전체 : /.*
    public static boolean matchesRegex(HttpServletRequest request, List<String> list) {
        return list.stream().anyMatch(request.getRequestURI()::matches);
    }

    /** AntPathMatcher 패턴 매칭 비교  */
    // 전체 : /**
    public static boolean matchesAntPath(HttpServletRequest request, List<String> list) {
        return list.stream().anyMatch(pattern -> antPathMatcher.match(pattern, request.getRequestURI()));
    }

}