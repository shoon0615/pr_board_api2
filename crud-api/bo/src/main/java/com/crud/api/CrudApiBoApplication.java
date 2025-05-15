package com.crud.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@ConfigurationPropertiesScan
//@SpringBootApplication
@SpringBootApplication(scanBasePackages = {"com.crud"})
/*@EnableJpaRepositories(basePackages = "com.crud.domain.domains.repository") // ✅ 도메인 레포지토리 위치 명시
@EntityScan(basePackages = "com.crud.domain.domains")                       // ✅ 엔티티 위치도 스캔 대상에 포함
@ComponentScan(basePackages = {
		"com.crud.api",       // API 모듈
		"com.crud.biz",       // 비즈니스 로직
		"com.crud.domain"     // 도메인 레이어 전체
})*/
public class CrudApiBoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrudApiBoApplication.class, args);
	}

	@Bean
	public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
		return new HiddenHttpMethodFilter();
	}

}
