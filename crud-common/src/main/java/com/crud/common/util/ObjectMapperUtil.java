/**
 * packageName  : com.crud.common.util
 * fileName     : ObjectMapperUtil
 * author       : SangHoon
 * date         : 2025-01-07
 * description  : ObjectMapper 는 내부적으로 초기화 작업이 많아 매번 객체 생성 시 성능 이슈가 있어 싱글톤 방식으로 개선
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-01-07          SangHoon             최초 생성
 */
package com.crud.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ObjectMapperUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    /** 직렬화/역직렬화 시 기준은 서버(Java) */
    static {
        mapper.registerModule(new JavaTimeModule());                            // LocalDate 관련 직렬화/역직렬화(@JsonFormat 적용)
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);        // 직렬화 시 null 이나 빈 값 포함 안함
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);  // 매칭되지 않는 변수가 있어도 에러 무시
        mapper.enable(SerializationFeature.INDENT_OUTPUT);                      // 콘솔에 출력 시 가독성있게 JSON 포맷팅
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);         // 날짜를 문자열로 직렬화
    }

    /**
     * <pre>
     * 직렬화 : 서버 정보를 클라이언트에서 활용할 수 있도록 변환
     * writeValue: Java(InputStream) -> JSON = API(DTO) -> UI(Request)
     * </pre>
     * @method       : serialization
     * @author       : SangHoon
     * @date         : 2025-04-10 AM 9:51
     */
    public static String serialization(Object value) throws JsonProcessingException {
        return mapper.writeValueAsString(value);
    }

    /**
     * <pre>
     * 역직렬화 : 클라이언트 정보를 서버에서 활용할 수 있도록 변환
     * readValue: JSON -> Java(InputStream) = UI(Request) -> API(DTO)
     * </pre>
     * @method       : deserialization
     * @author       : SangHoon
     * @date         : 2025-04-10 AM 9:51
     */
    public static <T> T deserialization(String content, Class<T> valueType) throws JsonProcessingException {
        return (T) mapper.readValue(content, valueType);
    }

    public static <T> T convertType(Object fromValue, Class<T> convertType){
        return (T) mapper.convertValue(fromValue, convertType);
    }

    /**
     * <pre>
     * 리플렉션을 통한 Type 변환
     * </pre>
     * @method       : convertType
     * @author       : SangHoon
     * @date         : 2025-02-06 AM 2:03
     * @sample
     * {@snippet :
     * T response = ObjectMapperUtil.convertType(request, new TypeReference<>() {});
     *}
     */
    public static <T> T convertType(Object fromValue, TypeReference<T> toValueTypeRef) {
        return (T) mapper.convertValue(fromValue, toValueTypeRef);
    }

}
