/**
 * packageName  : com.crud.api.config.exception
 * fileName     : ResponseStatusSetterAdvice
 * author       : SangHoon
 * date         : 2025-04-09
 * description  : ApiRecord 반환 객체 최종 확인 및 추가 처리(HttpStatus 적용)
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-04-09          SangHoon             최초 생성
 */
package com.crud.api.config.exception;

@Deprecated
/*@Slf4j
@RestControllerAdvice
public class ResponseStatusSetterAdvice implements ResponseBodyAdvice<ApiRecord<?>> {*/
public class ResponseStatusSetterAdvice {

    /** beforeBodyWrite 활성화 여부 */
    /*@Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return false;   // default
//        return returnType.getParameterType() == ApiRecord.class;
    }*/

    /** ApiRecord 일 경우, header(status) 에 httpStatus 인자값 적용(ResponseEntity 와 동일) */
    /*@Override
    public ApiRecord<?> beforeBodyWrite(
            ApiRecord body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response
    ) {
        HttpStatus status = body.httpStatus();
        response.setStatusCode(status);

        return body;
    }*/
}