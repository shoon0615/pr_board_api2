/**
 * packageName  : com.crud.common.dto.response
 * fileName     : ErrorResponse
 * author       : SangHoon
 * date         : 2025-01-18
 * description  : ApiResponse 의 error 파트 담당
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-01-18          SangHoon             최초 생성
 */
package com.crud.common.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

import static com.crud.common.enumeration.response.ResponseCode.ErrorCode;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
//@Builder
public class ErrorResponse {

    /** error 의 status */
    @NotNull
    private final int code;

    /** error 메시지 */
    @NotNull
    private final String errorMessage;

    /** error 상세 메시지 */
    @Nullable
    private final Object detailMessage;

    // 사이드에서 ErrorCode 의 code(status) 대신 Exception 의 httpStatus 로 사용 중
    /** (미사용) 실패용 객체 반환 메소드 */
    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getCode(), errorCode.getMessage(), null);
    }

    /** (미사용) 실패용 객체 반환 메소드(상세) */
    public static ErrorResponse of(ErrorCode errorCode, Object detailMessage) {
        return new ErrorResponse(errorCode.getCode(), errorCode.getMessage(), detailMessage);
    }

    /** 실패용 객체 반환 메소드 */
    public static ErrorResponse of(HttpStatus httpStatus, ErrorCode errorCode, Object detailMessage) {
        return new ErrorResponse(httpStatus.value(), errorCode.getMessage(), detailMessage);
    }

    /** 작업 중 오류용 객체 반환 메소드 */
    public static ErrorResponse of(String errorMessage) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMessage, null);
    }

}
