# SpringBoot-SideProject-CRUD_BackOffice_API_Extension
스프링 부트 기반의 백엔드 API 제공 프로젝트(확장)

## 설명
(기본) 기본 구조에 충실한 REST API 형식으로 구성되었습니다.<br>
(확장) 기본 구조에 실무 기능을 일부 적용하였습니다.<br>
(프로) 
- Model : H2 & Jpa
- View : `None`
- Controller : Java

---

### ⚙️ 개발 환경
- **IDE** : IntelliJ(Community - 2023.1)
- **Framework** : SpringBoot(3.x) -> v3.4.1
- **Language** : Java 17 -> Java 19(corretto-19)
- **DB** : H2 Database
- **ORM** : Jpa

### 🖥️ Tip
- **IntelliJ** : <a href="https://github.com/shoon0615/side-project/blob/master/README/IntelliJ.md">상세보기</a>

[//]: # (- **README** : <a href="https://github.com/shoon0615/side-project/tree/master/README/folder">상세보기</a>)
- **README** : <a href="https://github.com/shoon0615/pr_board_api2/tree/master/README/folder">상세보기</a>

### ⭐️ 추가 기능(기본)
- `QueryDSL` 적용
- `data.sql` 기본 data 생성
- `ResponseEntity<ApiResponse<T>>` 반환
- `BussinessException(=Custom Exception)` 및 `ExceptionHandler` 에러 처리
- `@Valid` 유효성 검증
- `ObjectMapper` DTO Convert
- `final` 인자 고정

### ⭐️ 추가 기능(심화)
- `AOP` 적용
- `Swagger` 문서화 -> 어노테이션을 통한 모듈화
- `@Valid` 메세지 변수화 -> `messages.properties` 적용
- `@ConfigurationProperties` yml 정보 호출 -> `Record` 형식
- `SpringSecurity` 보안 -> `JWT Token` 인증
- (진행중) `QueryDSL` 연관관계
- (진행예정) `Mapstruct` DTO Convert
- (진행예정) `RestClient` API 호출
- (진행예정) `@Valid` Date 관련 어노테이션 추가
- (미사용) ~~`@Validated` 그룹화 -> `조회용 Request`, `저장용 Request` 분리~~

### ⭐️ (추후 진행예정) 추가 기능(프로)
- (진행중) `Batch` 적용
- `CI/CD` 적용 -> `CI` 다수의 branch 및 Pull Request, `CD` Jenkins 또는 Git Actions
- (진행중) `Docker` 설정 -> 공유기 허용 또는 AWS
- `SVN` 서버 구축 -> 정적 파일 보관
- 기타 -> `NoSQL` `Kafka` `Redis`

### 📂 디렉토리 구조(폴더)
```bash
📦main
 ┣ 📂java
 ┃ ┗ 📂com
 ┃ ┃ ┗ 📂side_project
 ┃ ┃ ┃ ┗ 📂side-project
 ┃ ┃ ┃ ┃ ┣ 📂common
 ┃ ┃ ┃ ┃ ┃ ┣ 📂annotation
 ┃ ┃ ┃ ┃ ┃ ┣ 📂converter
 ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┣ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┣ 📂enumeration
 ┃ ┃ ┃ ┃ ┃ ┣ 📂exception
 ┃ ┃ ┃ ┃ ┃ ┣ 📂properties
 ┃ ┃ ┃ ┃ ┃ ┗ 📂repository
 ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┣ 📂exception
 ┃ ┃ ┃ ┃ ┃ ┣ 📂handler
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂aspect
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂filter
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂interceptor
 ┃ ┃ ┃ ┃ ┃ ┣ 📂jpa
 ┃ ┃ ┃ ┃ ┃ ┗ 📂logger
 ┃ ┃ ┃ ┃ ┃ ┣ 📂security
 ┃ ┃ ┃ ┃ ┃ ┗ 📂swagger
 ┃ ┃ ┃ ┃ ┣ 📂enumeration
 ┃ ┃ ┃ ┃ ┣ 📂in
 ┃ ┃ ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┣ 📂service
 ┃ ┃ ┃ ┃ ┃ ┗ 📂serviceImpl
 ┃ ┃ ┃ ┃ ┣ 📂out
 ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┣ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┗ 📂repositoryImpl
 ┃ ┃ ┃ ┃ ┣ 📂schema
 ┃ ┃ ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┗ 📂exception
 ┃ ┃ ┃ ┃ ┣ 📂util
 ┃ ┃ ┃ ┃ ┗ 📜PrCrudBoApiApplication.java
 ┗ 📂resources
 ┃ ┣ 📂messages
```

### 📜 디렉토리 구조(파일)
```bash
📦main
 ┣ 📂java
 ┃ ┗ 📂com
 ┃ ┃ ┗ 📂side_project
 ┃ ┃ ┃ ┗ 📂side-project
 ┃ ┃ ┃ ┃ ┣ 📂common
 ┃ ┃ ┃ ┃ ┃ ┣ 📂annotation
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ApiErrorCode.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ApiErrorCodes.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ApiSuccessCode.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ApiSuccessCodes.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂converter
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜BooleanToYnConverter.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂groups
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AllGroup.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜FindGroup.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜InsertGroup.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UpdateGroup.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ApiResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ErrorResponse.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜BaseEntity.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜BaseTimeEntity.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂enumeration
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂response
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜BaseCode.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ResponseCode.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AuthConstants.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜BaseDictionary.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂exception
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜BusinessException.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂properties
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜JwtProperties.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UrlProperties.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜JpaRepositoryExtension.java
 ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┣ 📂exception
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜GlobalExceptionHandler.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ResponseStatusSetterAdvice.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂handler
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂aspect
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜JpaExceptionAop.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂filter
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜JwtAuthenticationFilter.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜LoginFilter.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂interceptor
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RequestInterceptor.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂jpa
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜JpaConfig.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂logger
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜P6SpyConfig.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂security
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜SpringSecurityConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜WebMvcConfig.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂swagger
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜SwaggerConfig.java
 ┃ ┃ ┃ ┃ ┣ 📂enumeration
 ┃ ┃ ┃ ┃ ┃ ┗ 📜MemberRole.java
 ┃ ┃ ┃ ┃ ┣ 📂in
 ┃ ┃ ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RestCrudController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RestMemberController.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CrudRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜LoginRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜MemberRequest.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CrudService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜MemberService.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂serviceImpl
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CrudServiceImpl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberServiceImpl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserDetailsServiceImpl.java
 ┃ ┃ ┃ ┃ ┣ 📂out
 ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜CrudResponse.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CrudEntity.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜EmbMemberInfo.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜GrantedAuthorityEntity.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberEntity.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserDetailsEntity.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CrudEntityRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CrudEntityRepositoryCustom.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberAuthorityRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberEntityRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜MemberEntityRepositoryCustom.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂repositoryImpl
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CrudEntityRepositoryImpl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜MemberEntityRepositoryImpl.java
 ┃ ┃ ┃ ┃ ┣ 📂schema
 ┃ ┃ ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜RestCrudControllerDocs.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RestMemberControllerDocs.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CrudRequestDocs.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜MemberRequestDocs.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂exception
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ExampleHolder.java
 ┃ ┃ ┃ ┃ ┣ 📂util
 ┃ ┃ ┃ ┃ ┃ ┗ 📂utils
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜JwtUtil.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ObjectMapperUtil.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜RegexUtil.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RequestUriUtil.java
 ┃ ┃ ┃ ┃ ┗ 📜PrCrudBoApiApplication.java
 ┗ 📂resources
 ┃ ┣ 📂messages
 ┃ ┃ ┣ 📜messages.properties
 ┃ ┃ ┣ 📜messages_en_US.properties
 ┃ ┃ ┗ 📜messages_kr_KR.properties
 ┃ ┣ 📜application-url.yml
 ┃ ┣ 📜application.yml
 ┃ ┣ 📜data.sql
 ┃ ┗ 📜schema.sql
```
