/**
 * packageName  : com.crud.api.config.security
 * fileName     : WebMvcConfig
 * author       : SangHoon
 * date         : 2025-01-21
 * description  : AOP 적용 대상 설정
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-01-21          SangHoon             최초 생성
 */
package com.crud.api.config.security;

import com.crud.api.config.handler.interceptor.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final RequestInterceptor requestInterceptor;

    /**
     * AOP Interceptor 설정
     * @method       : addInterceptors
     * @author       : SangHoon
     * @date         : 2024-01-28 PM 12:48
     */
    /*public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor);
    }*/

    /**
     * <pre>
     * URL 싱글 매칭 허용(default: false)
     * 마지막 endPoint 가 "/" 면 "" 과 같은 url 로 취급
     * </pre>
     * @method       : configurePathMatch
     * @author       : SangHoon
     * @date         : 2024-12-26 PM 3:49
     */
    /*@Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseTrailingSlashMatch(true);
    }*/

}
