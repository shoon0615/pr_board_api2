/**
 * packageName  : com.crud.api.config.handler.interceptor
 * fileName     : RequestInterceptor
 * author       : SangHoon
 * date         : 2025-01-07
 * description  : AOP 접근 log 확인
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-01-07          SangHoon             최초 생성
 */
package com.crud.api.config.handler.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestInterceptor implements HandlerInterceptor {

    /**
     * 컨트롤러 접근 전 실행
     * @method       : preHandle
     * @author       : SangHoon
     * @date         : 2025-01-07 PM 3:21
     */
    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        log.info("==================== BEGIN ====================");
        log.info("Request URI ===> {}", request.getRequestURI());
        return true;
    }

    /**
     * 화면(View)에 전달하기 전 실행
     * @method       : postHandle
     * @author       : SangHoon
     * @date         : 2025-01-07 PM 3:21
     */
    @Override
    public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, ModelAndView modelAndView) throws Exception {
        log.info("==================== END ====================");
//        request.setAttribute("cartData", 1L);
    }

}
