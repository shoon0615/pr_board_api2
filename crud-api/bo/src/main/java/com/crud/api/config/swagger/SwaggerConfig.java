/**
 * packageName  : com.crud.api.config.swagger
 * fileName     : SwaggerConfig
 * author       : SangHoon
 * date         : 2025-01-10
 * description  : Swagger 환경설정 구성
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-01-10          SangHoon             최초 생성
 */
package com.crud.api.config.swagger;

import com.crud.api.common.enumeration.SwaggerDictionary;
import com.crud.common.annotation.ApiErrorCode;
import com.crud.common.annotation.ApiErrorCodes;
import com.crud.common.annotation.ApiSuccessCode;
import com.crud.common.annotation.ApiSuccessCodes;
import com.crud.common.enumeration.response.BaseCode;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.crud.common.enumeration.response.ResponseCode.ErrorCode;
import static com.crud.common.enumeration.response.ResponseCode.SuccessCode;
//import static com.crud.common.enumeration.response.ResponseCode.*;

/*
[추후 확인]
Spring Boot 3.x 이후 나온 @Configuration(proxyBeanMethods = false)의 개선된 버전
조건부 로딩(@ConditionalOnClass, @ConditionalOnProperty..) 등과 자주 사용되고, 자동 구성용 모듈 작성 시 자동 설정
@AutoConfiguration + META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports 등록
 */
//@AutoConfiguration || @EnableAutoConfiguration
@Configuration
public class SwaggerConfig {

    static {
        SpringDocUtils.getConfig()
                .replaceWithClass(LocalDate.class, String.class)
                .replaceWithClass(LocalTime.class, String.class)
                .replaceWithClass(YearMonth.class, String.class);
//                .addAnnotationsToIgnore(AuthenticationPrincipal.class, CookieValue.class);
    }

    @Bean
    public OpenAPI openAPI(@Value("v1.0.0") String appVersion) {
        return new OpenAPI()
//                .components(this.apiComponents())
                .components(new Components())
                .info(this.apiInfo(appVersion));
    }

    private Components apiComponents() {
        return new Components()
                // accessToken 이라는 스키마 만들어주기
                .addSecuritySchemes("accessToken", new SecurityScheme()
                        .name("accessToken")
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.HEADER)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                )
                // refreshToken 스키마 만들어주기
                .addSecuritySchemes("refreshToken", new SecurityScheme()
                        .name("refreshToken")
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.HEADER)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                );
    }

    private Info apiInfo(String appVersion) {
        return new Info()
                .title("[제목] Spring Boot REST API")
                .description("[내용] CRUD 테스트 Swagger")
                .termsOfService("http://swagger.io/terms/")
                .contact(this.apiContact())
                .license(this.apiLicense())
                .version(appVersion);
    }

    private Contact apiContact() {
        return new Contact()
                .name("dejavuhyo")
                .url("https://dejavuhyo.github.io/")
                .email("dejavuhyo@gmail.com");
    }

    private License apiLicense() {
        return new License()
                .name("Apache License Version 2.0")
                .url("http://www.apache.org/licenses/LICENSE-2.0");
    }

    /**
     * Swagger 의 @ApiResponse 관련 어노테이션 적용
     * @method       : customize
     * @author       : SangHoon
     * @date         : 2025-01-23 AM 2:06
     */
    @Bean
    public OperationCustomizer customize() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            ApiErrorCodes apiErrorCodes = handlerMethod.getMethodAnnotation(ApiErrorCodes.class);

            // @ApiErrorCodes 어노테이션이 붙어있다면
            if (apiErrorCodes != null) {
                this.generateResponseCodeResponse(operation, apiErrorCodes.value());
            } else {
                ApiErrorCode apiErrorCode = handlerMethod.getMethodAnnotation(ApiErrorCode.class);

                // @ApiErrorCodes 없이 @ApiErrorCode 어노테이션만 붙어있다면
                if (apiErrorCode != null) {
                    this.generateResponseCodeResponse(operation, apiErrorCode.value());
                }
            }

            // 성공에 대한 Swagger 도 추가(ex: 중복체크 등..)
            ApiSuccessCodes apiSuccessCodes = handlerMethod.getMethodAnnotation(ApiSuccessCodes.class);

            if (apiSuccessCodes != null) {
                this.generateResponseCodeResponse(operation, apiSuccessCodes.value());
            } else {
                ApiSuccessCode apiSuccessCode = handlerMethod.getMethodAnnotation(ApiSuccessCode.class);
                if (apiSuccessCode != null) {
                    this.generateResponseCodeResponse(operation, apiSuccessCode.value());
                }
            }

            return operation;
        };
    }

    /** 여러 개의 응답값 예시 추가 */
    private void generateResponseCodeResponse(Operation operation, BaseCode[] responseCodes) {
        ApiResponses responses = operation.getResponses();

        // ExampleHolder(에러 응답값) 객체를 만들고 에러 코드별로 그룹화
        Map<Integer, List<ExampleHolder>> statusWithExampleHolders = Arrays.stream(responseCodes)
                .map(responseCode -> ExampleHolder.builder()
                        .holder(this.getSwaggerExample(responseCode))
//                        .code(responseCodes.getStatus().value())
                        .code(responseCode.getCode())
                        .name(responseCode.name())
                        .build()
                )
                .collect(Collectors.groupingBy(ExampleHolder::getCode));

        // ExampleHolders 를 ApiResponses 에 추가
        this.addExamplesToResponses(responses, statusWithExampleHolders);
    }

    /** 단일 응답값 예시 추가 */
    private void generateResponseCodeResponse(Operation operation, BaseCode responseCode) {
        ApiResponses responses = operation.getResponses();

        // ExampleHolder 객체 생성 및 ApiResponses 에 추가
        ExampleHolder exampleHolder = ExampleHolder.builder()
                .holder(this.getSwaggerExample(responseCode))
                .name(responseCode.name())
//                .code(responseCode.getStatus().value())
                .code(responseCode.getCode())
                .build();

        this.addExamplesToResponses(responses, exampleHolder);
    }

    /** Response 형태의 예시 객체 생성 */
    private Example getSwaggerExample(BaseCode responseCode) {
        Example example = new Example();

        if (responseCode instanceof ErrorCode) {
//            ErrorResponse errorResponse = ErrorResponse.of((ErrorCode) responseCode);     // body(error) 만 조회
            com.crud.common.dto.response.ApiResponse errorResponse = com.crud.common.dto.response.ApiResponse.of((ErrorCode) responseCode);
            example.setValue(errorResponse);
            return example;
        }

        com.crud.common.dto.response.ApiResponse successResponse = com.crud.common.dto.response.ApiResponse.of((SuccessCode) responseCode);
        example.setValue(successResponse);
        return example;
    }

    /** 여러 개의 ExampleHolder 를 ApiResponses 에 추가 */
    private void addExamplesToResponses(ApiResponses responses, Map<Integer, List<ExampleHolder>> statusWithExampleHolders) {
        // remove default 200 code
//        if (responses != null && responses.containsKey("200")) {
//        if (responses != null && responses.containsKey(BaseDictionary.SUCCESS_CODE)) {
        if (responses != null && responses.containsKey(String.valueOf(HttpStatus.OK.value()))) {
            responses.remove(HttpStatus.OK.value());
        }

        statusWithExampleHolders.forEach((status, v) -> {
            // io.swagger.v3.oas.models.media 의 모듈 사용
            Content content = new Content();
            MediaType mediaType = new MediaType();
            ApiResponse apiResponse = new ApiResponse();

            v.forEach(exampleHolder -> mediaType.addExamples(
                    exampleHolder.getName(),
                    exampleHolder.getHolder()
            ));
//            content.addMediaType("application/json", mediaType);
            content.addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE, mediaType);
            apiResponse.setContent(content);
            apiResponse.description(status == HttpStatus.OK.value() ? SwaggerDictionary.SUCCESS : SwaggerDictionary.ERROR);
            responses.addApiResponse(String.valueOf(status), apiResponse);
        });
    }

    /** 단일 ExampleHolder 를 ApiResponses 에 추가 */
    private void addExamplesToResponses(ApiResponses responses, ExampleHolder exampleHolder) {
        // remove default 200 code
        if (responses != null && responses.containsKey(String.valueOf(HttpStatus.OK.value()))) {
            responses.remove(HttpStatus.OK.value());
        }

        Content content = new Content();
        MediaType mediaType = new MediaType();
        ApiResponse apiResponse = new ApiResponse();

        mediaType.addExamples(exampleHolder.getName(), exampleHolder.getHolder());
        content.addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE, mediaType);
        apiResponse.content(content);
        apiResponse.description(exampleHolder.getCode() == HttpStatus.OK.value() ? SwaggerDictionary.SUCCESS : SwaggerDictionary.ERROR);
        responses.addApiResponse(String.valueOf(exampleHolder.getCode()), apiResponse);
    }

}
