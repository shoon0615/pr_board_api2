/**
 * packageName  : com.crud.common.util
 * fileName     : TddUtil
 * author       : SangHoon
 * date         : 2025-05-14
 * description  : 테스트(TDD) TestDrivenDevelopment 지원 라이브러리
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-05-14          SangHoon             최초 생성
 */
package com.crud.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.experimental.UtilityClass;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Field;
import java.util.Map;

@UtilityClass
public class TddUtil {

    /**
     * 리플렉션을 통한 Type 변환
     * @method       : convertType
     * @author       : SangHoon
     * @date         : 2025-05-14 AM 2:13
     */
    public MultiValueMap<String, String> convertType(Object fromValue) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        Class<?> clazz = fromValue.getClass();

        while (clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    Object value = field.get(fromValue);
                    if (value != null) {
                        map.add(field.getName(), String.valueOf(value));
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Field access error: " + field.getName(), e);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return map;
    }

    /**
     * UrlMapping 형식의 QueryString 반환
     * @method       : convertQueryString
     * @author       : SangHoon
     * @date         : 2025-05-14 AM 2:13
     */
    public String convertQueryString(String path, Object fromValue) {
        Map<String, Object> requestMap = ObjectMapperUtil.convertType(fromValue, new TypeReference<>() {});
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(path);
        if(requestMap != null) {
            for (Map.Entry<String, ?> entry : requestMap.entrySet()) {
                uriBuilder.queryParam(entry.getKey(), entry.getValue());
            }
        }
//        UriComponents builder = uriBuilder.build().encode(StandardCharsets.UTF_8);
        UriComponents builder = uriBuilder.build();
        return builder.toUriString();
    }

    public String convertQueryString(String url, String path, Object fromValue) {
        return convertQueryString(url.concat(path), fromValue);
    }

}
