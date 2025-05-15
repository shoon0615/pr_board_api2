/**
 * packageName  : com.crud.api.config.handler.aspect
 * fileName     : JpaExceptionAop
 * author       : SangHoon
 * date         : 2025-01-21
 * description  : (미사용) Jpa 관련 모든 Exception 후속 처리
 *                  모든 Exception 에 공통적으로 처리할 작업 작성
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-01-21          SangHoon             최초 생성
 */
package com.crud.api.config.handler.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Deprecated
@Slf4j
@Aspect
@Component
public class JpaExceptionAop {

    /** 해당 package 내에서 Jpa Exception 이 발생하는 경우, AOP 실행 */
    // Dirty Checking 의 경우 @Transactional 을 벗어날 때 commit 되며 오류가 발생하기에 service 가 아닌 controller 에서 검증 가능
    @Pointcut("execution(* com.side_project.side-project.in.controller.*.*(..))")
    public void pointcutJpaExceptionMethods() {}

    /**
     * <pre>
     * (미사용) -> 테스트는 성공, AOP 가 우선순위가 더 높아 AOP(BusinessException 로 전환) -> ExceptionHandler(DataIntegrityViolationException 검증 안됨) 발생
     * 비즈니스 로직의 흐름 제어를 위해 Exception 은 모두 handler 를 통해 처리하고,
     * AOP 에서는 로깅이나 추적에만 사용하는 것이 일반적이라고 함
     * </pre>
     * @method       : handleJpaException
     * @author       : SangHoon
     * @date         : 2025-01-22 PM 3:56
     */
    // Dirty Checking 외에도 범용적인 오류일 수 있기에 추후 확인 및 수정이 필요할 수 있음
    /*@AfterThrowing(pointcut = "pointcutJpaExceptionMethods()", throwing = "ex")
    public void handleJpaException(DataIntegrityViolationException ex) {    // convertHibernateAccessException
        log.error("handleJpaException : DataIntegrityViolationException");
        log.error("method: {}, error: {}", ex.getStackTrace()[0].getMethodName(), this.getDetailMessage(ex.getMessage()));
        log.error(ex.getMessage());
//        throw new BusinessException(ex.getMessage());
        throw new BusinessException(ResponseCode.ErrorCode.SAVE_ERROR);
    }*/

    /*@AfterThrowing(pointcut = "execution(* com.side_project.side-project.in.controller.*.*(..))", throwing = "ex")
    public void handleJpaException(Exception ex) {
        if (ex instanceof PersistenceException) {
            log.error("handleJpaException : PersistenceException");
//            throw new BusinessException(ex.getMessage());
        } else if (ex instanceof DataIntegrityViolationException) {
            log.error("handleJpaException : DataIntegrityViolationException");
            log.error("method: {}, error: {}", ex.getStackTrace()[0].getMethodName(), this.getDetailMessage(ex.getMessage()));
            log.error(ex.getMessage());
//            throw new BusinessException(ex.getMessage());
            throw new BusinessException(ResponseCode.ErrorCode.SAVE_ERROR);
        }
    }*/

    private String getDetailMessage(String message) {
        return message.substring(message.indexOf("["), message.indexOf(";")).replaceAll("\"", "'").concat("]");
    }

}
