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

@Deprecated
public class GlobalExceptionHandler2 {

}

//import com.crud.common.dto.response.ApiResponse;
//import com.crud.common.dto.response.ErrorResponse;
//import com.crud.common.exception.BusinessException;
//import io.jsonwebtoken.JwtException;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.support.DefaultMessageSourceResolvable;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.HttpStatusCode;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.validation.method.MethodValidationException;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.MissingPathVariableException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.method.annotation.HandlerMethodValidationException;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//import org.springframework.web.servlet.resource.NoResourceFoundException;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static com.crud.common.enumeration.response.ResponseCode.ErrorCode;
//
//@Slf4j
//@RestControllerAdvice
//@RequiredArgsConstructor
//public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
//
//    /** Service 작업 중 오류가 발생한 경우 */
//    @ExceptionHandler(BusinessException.class)
//    protected ResponseEntity<ApiResponse<ErrorResponse>> handleBusiness(final BusinessException ex, final WebRequest request) {
////        request.getDescription(false);   // 시도한 클라이언트 정보 호출 여부(IP 포함) -> 보안상 사용 X
//        return ApiResponse.fail(ex.getMessage());
//    }
//
//    /** Jpa 작업 중 오류가 발생한 경우(SQL 혹은 Data 가 잘못된 경우) -> try~catch 로 대체해야 할듯 */
//    // Dirty Checking 은 @Transactional 을 벗어날 때 commit 되며 오류가 발생하기에 Service 에서 검증이 안되어 Exception 으로 처리
//    // Dirty Checking 외에도 범용적인 오류일 수 있기에 추후 확인 및 수정이 필요할 수 있음
//    /*@ExceptionHandler(DataIntegrityViolationException.class)    // convertHibernateAccessException
//    protected ResponseEntity<ApiResponse<ErrorResponse>> handleBusiness(final DataIntegrityViolationException ex, final WebRequest request) {
//        String errorMessage = ex.getMessage().substring(ex.getMessage().indexOf("["), ex.getMessage().indexOf(";")).replaceAll("\"", "'").concat("]");
//        log.error("method: {}, error: {}", ex.getStackTrace()[0].getMethodName(), errorMessage);
////        return ApiResponse.fail(HttpStatus.BAD_REQUEST, ErrorCode.NOT_VALID, errorMessage);   // 보안 상 제거
//        return ApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.SAVE_ERROR);
//    }*/
//
//    /** 존재하지 않는 데이터를 조회한 경우 */
//    // findById 만 대상이 아닌 Optional 객체의 조회 실패 시의 모든 상황에 발생하기에 orElseThrow(BusinessException) 사용을 권장
//    /*@ExceptionHandler(NoSuchElementException.class)
//    protected ResponseEntity<ApiResponse<ErrorResponse>> handleBusiness(final NoSuchElementException ex, final WebRequest request) {
//        return ApiResponse.fail(HttpStatus.NOT_FOUND, ErrorCode.FIND_ERROR, ex.getMessage());
//    }*/
//
//    /** 존재하지 않는 요청이 들어온 경우 */
//    @Override
//    protected ResponseEntity handleNoResourceFoundException(NoResourceFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
//        return ApiResponse.fail(status, ErrorCode.NOT_FOUND);
//    }
//
//    /** @Valid 검증에 실패한 경우 */
//    @Override
//    protected ResponseEntity handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
//        List<String> errors = ex.getBindingResult().getFieldErrors()
//                .stream()
//                .map(DefaultMessageSourceResolvable::getDefaultMessage)
//                .toList();
//        return ApiResponse.fail(status, ErrorCode.NOT_VALID, Arrays.deepToString(errors.toArray()));
//    }
//
//    /** @Validated 검증에 실패한 경우(@RequestParam, @PathVariable) */
//    @Override
//    protected ResponseEntity<Object> handleHandlerMethodValidationException(HandlerMethodValidationException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
//        log.error("handleHandlerMethodValidationException : HandlerMethodValidationException");
//        return super.handleHandlerMethodValidationException(ex, headers, status, request);
//    }
//
//    /** @Validated 검증에 실패한 경우(@RequestBody) */
//    // ConstraintViolationException: Spring 5.x 이전, MethodValidationException: Spring 6.x 이상
//    @Override
//    protected ResponseEntity<Object> handleMethodValidationException(MethodValidationException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        log.error("handleMethodValidationException : MethodValidationException");
//        return super.handleMethodValidationException(ex, headers, status, request);
//    }
//
//    /** @PathVariable 누락된 경우 */
//    @Override
//    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
//        log.error("handleMissingPathVariable : MissingPathVariableException");
//        return super.handleMissingPathVariable(ex, headers, status, request);
//    }
//
//    /** 인증 중 오류가 발생한 경우 */
//    @ExceptionHandler(AuthenticationException.class)
//    protected ResponseEntity handleAuthentication(final AuthenticationException ex, final WebRequest request) {
//        return ApiResponse.fail(HttpStatus.UNAUTHORIZED, ErrorCode.UNAUTHORIZED);
//    }
//
//    /** 인증은 되었으나 권한이 없는 경우 */
//    @ExceptionHandler(AccessDeniedException.class)
//    protected ResponseEntity handleAccessDenied(final AccessDeniedException ex, final WebRequest request) {
//        return ApiResponse.fail(HttpStatus.FORBIDDEN, ErrorCode.FORBIDDEN);
//    }
//
//    /** JWT Token 검증 중 오류가 발생한 경우 */
//    @ExceptionHandler(JwtException.class)
//    protected ResponseEntity handleJwt(final JwtException ex, final WebRequest request) {
//        return ApiResponse.fail(ex.getMessage());
//    }
//
//    /** 기타(그 외) */
//    @Override
//    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
//        log.error("handleExceptionInternal : Exception");
//        log.error("method: {}, body: {}, error: {}", ex.getStackTrace()[0].getMethodName(), body, ex.getMessage());
//        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
//    }
//
//}