/**
 * packageName  : com.crud.api.config.exception
 * fileName     : GlobalExceptionHandler
 * author       : SangHoon
 * date         : 2025-01-08
 * description  : Spring MVC 관련 모든 Exception 처리
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-01-08          SangHoon             최초 생성
 */
package com.crud.api.config.exception;

import com.crud.common.dto.response.ApiResponse;
import com.crud.common.dto.response.ErrorResponse;
import com.crud.common.exception.BusinessException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Arrays;
import java.util.List;

import static com.crud.common.enumeration.response.ResponseCode.ErrorCode;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    /** Service 작업 중 오류가 발생한 경우 */
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ApiResponse<ErrorResponse>> handleBusiness(final BusinessException ex, final WebRequest request) {
//        request.getDescription(false);   // 시도한 클라이언트 정보 호출 여부(IP 포함) -> 보안상 사용 X
        return ApiResponse.fail(ex.getMessage());
    }

    /** @Valid 검증에 실패한 경우 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final WebRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
//        return ApiResponse.fail(status, ErrorCode.NOT_VALID, Arrays.deepToString(errors.toArray()));
        return ApiResponse.fail(HttpStatus.BAD_GATEWAY, ErrorCode.NOT_VALID, Arrays.deepToString(errors.toArray()));
    }

    /** 기타(그 외) */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> handleExceptionInternal(final Exception ex, final WebRequest request) {
        log.error("[Method] {}", ex.getStackTrace()[0].getMethodName(), ex);
        return ApiResponse.fail(ex.getMessage());
    }

}