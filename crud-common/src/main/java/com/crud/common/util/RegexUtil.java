/**
 * packageName  : com.crud.common.util
 * fileName     : RegexUtil
 * author       : SangHoon
 * date         : 2025-01-10
 * description  : 패턴 확인 및 @Valid 를 체크하기 위한 정규식 방식 라이브러리
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-01-10          SangHoon             최초 생성
 */
package com.crud.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

// messages.properties 에서 가져오기??
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegexUtil {

    /** 특수문자 포함 여부 */
    public static boolean hasSpecialChar(String str){
        return str.matches ("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝]*");
    }

    /** 숫자/영문으로만 구성 여부 */
    public static boolean onlyNumericWithAlphabet(String str){
        return str.matches("^[a-zA-Z0-9]*");
    }

    /** 숫자로만 구성 여부 */
    public static boolean isNumeric(String str){
        return str.matches("^[0-9]*$");
    }

    /** 영문으로만 구성 여부 */
    public static boolean isAlphabet(String str){
        return str.matches("^[a-zA-Z]*$");
    }

    /** 영문으로만 구성 여부(대문자) */
    public static boolean isUpper(String str){
        return str.matches("^[A-Z]*$");
    }

    /** 영문으로만 구성 여부(소문자) */
    public static boolean isDowner(String str){
        return str.matches("^[a-z]*$");
    }

    /** 한글로만 구성 여부 */
    public static boolean isKorean(String str){
        return str.matches("[가-힣]*$");
    }

    /** URL 형식 여부 */
    public static boolean isUrl(String str){
        return str.matches("(http[s]?:\\/\\/)([a-zA-Z0-9]+)\\.[a-z]+([a-zA-Z0-9.?#]+)?");
    }

    /** URL Path 형식 여부 */
    public static boolean isUrlPath(String str){
        return str.matches("(http[s]?:\\/\\/)([^\\/\\s]+\\/)(.*)");
    }

    /** IP 형식 여부 */
    public static boolean isIp(String str){
        return str.matches("([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})");
    }

    /** 날짜 형식 여부(yyyyMMdd) */
    public static boolean isDate(String str){
        return str.matches("^\\d{4}.\\d{2}.\\d{2}$");
    }

    /** 날짜 형식 여부(포맷) */
    public static boolean isDate(String str, String separator){
        String quotedSeparator = Pattern.quote(separator);
        String regex = String.format("^\\d{4}%s\\d{2}%s\\d{2}$", quotedSeparator, quotedSeparator);
        return str.matches(regex);
    }

    /** UUID 형식 여부 */
    public static boolean isUUID(String str){
        return str.matches("[a-f0-9]{8}(?:-[a-f0-9]{4}){4}[a-f0-9]{8}");
    }

}
