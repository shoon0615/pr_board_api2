/**
 * packageName  : com.crud.api.common.enumeration
 * fileName     : SwaggerDictionary
 * author       : SangHoon
 * date         : 2025-05-12
 * description  : 어노테이션 및 기본 설정에 필요한 값들 초기화용
 *                  BaseDictionary -> SwaggerDictionary
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-05-12          SangHoon             최초 생성
 */
package com.crud.api.common.enumeration;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SwaggerDictionary {

    // 추후 구조가 너무 복잡해질 경우 리팩토링 필요
    /*public class Group {
        public String SUCCESS_CODE = "200";
    }*/

    public final String SUCCESS_CODE = "200";
    public final String SUCCESS = "성공";
    public final String ERROR = "실패";

    public final String FIND = "조회(단건)";
    public final String FIND_ALL = "조회(전체)";
    public final String FIND_PAGE = "조회(페이징)";
    public final String INSERT = "입력";
    public final String UPDATE = "수정";
    public final String DELETE = "삭제";

    public final String LOGIN = "로그인";
    public final String SIGNUP = "회원가입";

}
