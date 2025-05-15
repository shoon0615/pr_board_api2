/**
 * packageName  : com.crud.common.annotation
 * fileName     : ApiErrorCode
 * author       : SangHoon
 * date         : 2025-01-23
 * description  : Swagger 의 @ApiResponse(exception) 자동화 어노테이션
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-01-23          SangHoon             최초 생성
 */
package com.crud.common.annotation;

import com.crud.common.enumeration.response.ResponseCode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiErrorCode {

//    Class<? extends ResponseCode.ErrorCode> value();
    ResponseCode.ErrorCode value();

}
