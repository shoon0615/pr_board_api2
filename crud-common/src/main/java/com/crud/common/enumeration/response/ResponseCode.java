/**
 * packageName  : com.crud.common.enumeration.response
 * fileName     : ResponseCode
 * author       : SangHoon
 * date         : 2025-01-21
 * description  : ApiResponse 의 message 파트 담당
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-01-21          SangHoon             최초 생성
 */
package com.crud.common.enumeration.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

public class ResponseCode {

    // 실무에선 custom 된 code 가 사용되지만 사이드에선 HttpStatus 의 status 로 모두 처리 예정(메시지만 custom)

    @Getter
    @RequiredArgsConstructor
    public enum SuccessCode implements BaseCode {

        /** 성공 */
        @JsonProperty("성공했습니다.")    // Swagger 조회용
//        OK("B001", HttpStatus.OK, "성공했습니다.");
        OK(HttpStatus.OK, "성공했습니다.");

        /** statusCode */
//        private final int code;

        /** status */
//        @Getter(AccessLevel.NONE)
        private final HttpStatus status;

        /** 결과 메시지 */
        private final String message;

        /** (임시) customCode 대신 등록된 HttpStatus 의 statusCode 반환 */
//        @Deprecated(forRemoval = true)
        @Override
        public int getCode() {
            return this.status.value();
        }

    }

    @Getter
    @RequiredArgsConstructor
    public enum ErrorCode implements BaseCode {

        /** 유효성 검증 실패 */
        NOT_VALID(HttpStatus.BAD_REQUEST, "유효성 검증에 실패했습니다."),

        /** 인증 실패 */
        UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증에 실패했습니다."),

        /** 접근 권한 없음 */
        FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

        /** 존재하지 않는 API */
        NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 API 입니다."),

        /** 지원하지 않는 HTTP Method */
        METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP Method 요청입니다."),

        /** 조회 중 오류 발생 */
        FIND_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "조회 중 오류가 발생했습니다."),

        /** 저장 중 오류 발생 */
        SAVE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "저장 중 오류가 발생했습니다."),

        /** 삭제 중 오류 발생 */
        DELETE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "삭제 중 오류가 발생했습니다."),

        /** 작업 중 오류 발생 */
        INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "작업 중 오류가 발생했습니다.");

        /** statusCode */
//        private final int code;

        /** status */
        private final HttpStatus status;

        /** 결과 메시지 */
        private final String message;

        /** 결과 메시지 출력(직접 선언) */
        public String getMessage(String message) {
            return StringUtils.hasText(message) ? message : this.getMessage();
        }

        @Override
        public int getCode() {
            return this.status.value();
        }

    }

}