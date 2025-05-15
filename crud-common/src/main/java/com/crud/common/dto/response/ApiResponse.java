/**
 * packageName  : com.crud.common.dto.response
 * fileName     : ApiResponse
 * author       : SangHoon
 * date         : 2025-01-18
 * description  : ResponseEntity 의 커스텀 반환 라이브러리
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-01-18          SangHoon             최초 생성
 */
package com.crud.common.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

import static com.crud.common.enumeration.response.ResponseCode.SuccessCode;
import static com.crud.common.enumeration.response.ResponseCode.ErrorCode;
//import static com.crud.common.enumeration.response.ResponseCode.*;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {

    // (미사용) ResponseEntity 의 header(status) + ErrorResponse 의 status 로 구상 예정
    /** @deprecated status(header) */
    @Deprecated
    @Getter(AccessLevel.NONE)
    @JsonIgnore
    private HttpStatus httpStatus;

    /** api 성공 여부 */
    private final boolean success;

    /** 정보(데이터) */
    @Nullable
    private final T data;

    /** error 정보 */
    @Nullable
    private final ErrorResponse error;

    /** api 완료 시간 */
    @JsonIgnore
    private LocalDateTime timestamp = LocalDateTime.now();

    private ApiResponse(T data, ErrorResponse error) {
        this.success = error == null ? true : false;
        this.data = data;
        this.error = error;
    }

    /** 성공용 객체 반환 메소드 */
//    private static <T> ApiResponse<T> of(T data) {    // Swagger 조회로 인해 public 으로 변경
    public static <T> ApiResponse<T> of(T data) {
        return new ApiResponse<>(data, null);
    }

    /** 실패용 객체 반환 메소드 */
    private static ApiResponse<ErrorResponse> of(HttpStatus status, ErrorCode errorCode, Object detailMessage) {
        return new ApiResponse<>(null, ErrorResponse.of(status, errorCode, detailMessage));
    }

    /** 실패용 객체 반환 메소드(심화) */
    public static ApiResponse<ErrorResponse> of(ErrorCode errorCode) {
        return new ApiResponse<>(null, ErrorResponse.of(errorCode));
    }

    /** 작업 중 오류용 객체 반환 메소드 */
    private static ApiResponse<ErrorResponse> of(String errorMessage) {
        return new ApiResponse<>(null, ErrorResponse.of(errorMessage));
    }

    /** 성공 */
    public static <T> ResponseEntity<ApiResponse<T>> ok(@Nullable final T data) {
        return ResponseEntity.ok(ApiResponse.of(data));
    }

    /** 성공(기본) */
    public static ResponseEntity<ApiResponse<Object>> ok() {
//        return ApiResponse.ok(null);
        return ApiResponse.ok(SuccessCode.OK.getMessage());
    }

    /** 성공(심화) */
    public static ResponseEntity<ApiResponse<Object>> ok(final SuccessCode successCode) {
        return ApiResponse.ok(successCode.getMessage());
    }

    /** 실패(상세) */
    public static ResponseEntity<ApiResponse<ErrorResponse>> fail(final HttpStatus status, final ErrorCode errorCode, final Object detailMessage) {
//        return ResponseEntity.status(status.value()).body(ApiResponse.of(status, errorCode, detailMessage));
        return ResponseEntity.ok(ApiResponse.of(status, errorCode, detailMessage));
    }

    /** 실패(상세) */
    public static ResponseEntity<ApiResponse<ErrorResponse>> fail(final HttpStatusCode status, final ErrorCode errorCode, final Object detailMessage) {
        return ApiResponse.fail(HttpStatus.valueOf(status.value()), errorCode, detailMessage);
    }

    /** 실패(기본) */
    public static ResponseEntity<ApiResponse<ErrorResponse>> fail(final HttpStatus status, final ErrorCode errorCode) {
        return ApiResponse.fail(status, errorCode, null);
    }

    /** 실패(기본) */
    public static ResponseEntity<ApiResponse<ErrorResponse>> fail(final HttpStatusCode status, final ErrorCode errorCode) {
        return ApiResponse.fail(HttpStatus.valueOf(status.value()), errorCode, null);
    }

    /** 작업 중 오류 발생 */
    public static ResponseEntity<ApiResponse<ErrorResponse>> fail(final String errorMessage) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.of(errorMessage));
        return ResponseEntity.ok(ApiResponse.of(errorMessage));
    }

}
