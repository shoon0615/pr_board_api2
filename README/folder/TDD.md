# SpringBoot-SideProject-TestDrivenDevelopment
스프링 부트 TDD

## 기본 구조
- **기본 패턴 - Given/When/Then**
1. `given` 요청 데이터 정의(=선언)
   - `@BeforeEach` `@ParameterizedTest` `@WithMockUser`
   - `Mock` 단위 테스트는 가짜로 예상 결과 값까지 정의 가능
2. `when` 요청 테스트 실행
3. `then` 응답 결과 확인

- **단위 테스트(Slice/Unit Test) - 가짜 객체**
1. 격리된 환경에서 필요한 부분만 정상적으로 동작하는지 빠르고 효율적으로 검증하는 목적의 테스트
2. [Repository] `@DataJpaTest` 인메모리 DB(H2) 를 통해 Repository 작업 테스트
3. [Service] `@ExtendWith(MockitoExtension.class)` Mock(가짜 객체) 을 통해 빠른 Service 검증 이후, 예상 결과값 및 예외 처리(Exception) 확인
4. [Controller] `@WebMvcTest(RestController.class)` MockBean(가짜 객체) 을 통해 Controller 까지의 작업 검증 이후, 클라이언트에 전달될 예상 결과값(JSON) 확인

- **통합 테스트(Integration Test) - 실제 객체**
1. Spring 의 전체 컨텍스트(Bean) 를 로드하여 실제 환경과 동일한 조건에서 테스트 수행
2. [All] `@SpringBootTest` 실제 DB 확인 가능할 정도의 서비스만 호출하고, 그 외엔 Controller 를 통해 실제 환경처럼 진행하여 확인

---

## 🖥️ Tip
- `JdbcTest` JPA 가 아닌 JDBC 방식의 DB 테스트
- `RestClientTest` 외부 API 연동 테스트(RestTemplate, RestClient, Feign...)

---

### ⚙️ 개발 환경
- **IDE** : IntelliJ(Community - 2024.3.5)
- **Framework** : SpringBoot(3.x) -> v3.4.4
- **Language** : Java 17 -> Java 19(corretto-19)
- **DB** : H2 Database
- **ORM** : Jpa

### 📂 디렉토리 구조(폴더)
```bash
📦src
┣ 📂main
┃ ┣ 📂generated
┃ ┃ ┗ 📂com
┃ ┃ ┃ ┗ 📂example
┃ ┃ ┃ ┃ ┗ 📂jpa_test
┃ ┃ ┃ ┃ ┃ ┣ 📂common
┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂entity
┃ ┃ ┃ ┃ ┃ ┗ 📂test
┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂out
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂entity
┃ ┣ 📂java
┃ ┃ ┗ 📂com
┃ ┃ ┃ ┗ 📂example
┃ ┃ ┃ ┃ ┗ 📂jpa_test
┃ ┃ ┃ ┃ ┃ ┣ 📂common
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂converter
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂entity
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂properties
┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂repository
┃ ┃ ┃ ┃ ┃ ┣ 📂config
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂exception
┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂jpa
┃ ┃ ┃ ┃ ┃ ┣ 📂test
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂in
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂controller
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂service
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂serviceImpl
┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂out
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂entity
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂repositoryImpl
┃ ┃ ┃ ┃ ┃ ┣ 📂util
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂mapstruct
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂mybatis
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂qualifier
┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂utils
┃ ┃ ┃ ┃ ┃ ┗ 📜JpaTestApplication.java
┃ ┗ 📂resources
┗ 📂test
┃ ┗ 📂java
┃ ┃ ┗ 📂com
┃ ┃ ┃ ┗ 📂example
┃ ┃ ┃ ┃ ┗ 📂jpa_test
┃ ┃ ┃ ┃ ┃ ┣ 📂tdd
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂controller
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂service
┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TestIntegrationTest.java
┃ ┃ ┃ ┃ ┃ ┗ 📜JpaTestApplicationTests.java
```

### 📂 디렉토리 구조(파일)
```bash
📦src
┣ 📂main
┃ ┣ 📂generated
┃ ┃ ┗ 📂com
┃ ┃ ┃ ┗ 📂example
┃ ┃ ┃ ┃ ┗ 📂jpa_test
┃ ┃ ┃ ┃ ┃ ┣ 📂common
┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂entity
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜QBaseEntity.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜QBaseMemberEntity.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜QBaseTimeEntity.java
┃ ┃ ┃ ┃ ┃ ┗ 📂test
┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂out
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂entity
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂test
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂cpk
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜QTestEmbIdEntity.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜QTestIdClassEntity.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂emb
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜QAddEmbColumn.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜QAddEmbId.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜QTestEmbEntity.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂extend
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜QAddDbColumn.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜QAddDbId.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜QTestExtendsEntity.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂id
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜QTestEntity.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜QTestIdEntity.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂join
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜QTestJoinEntity.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜QTestSingleEntity.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜QTestSubJoinEntity.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜QTestSubSingleEntity.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂mapped_by
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜QTestCountryEntity.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜QTestPeopleEntity.java
┃ ┣ 📂java
┃ ┃ ┗ 📂com
┃ ┃ ┃ ┗ 📂example
┃ ┃ ┃ ┃ ┗ 📂jpa_test
┃ ┃ ┃ ┃ ┃ ┣ 📂common
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂converter
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜BooleanToYnConverter.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ApiResponse.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂entity
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜BaseEntity.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜BaseMemberEntity.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜BaseTimeEntity.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂properties
┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂repository
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜JpaRepositoryExtension.java
┃ ┃ ┃ ┃ ┃ ┣ 📂config
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂exception
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜GlobalExceptionHandler.java
┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂jpa
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AuditorAwareImpl.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜JpaConfig.java
┃ ┃ ┃ ┃ ┃ ┣ 📂test
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂in
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂controller
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RestTestController.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TestRequest.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂service
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TestService.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂serviceImpl
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TestServiceImpl.java
┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂out
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TestResponse.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂entity
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂test
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂cpk
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜TestEmbIdEntity.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TestIdClassEntity.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂emb
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AddEmbColumn.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AddEmbId.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TestEmbEntity.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂extend
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AddDbColumn.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AddDbId.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AddJavaColumn.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TestExtendsEntity.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂id
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜TestEntity.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜TestIdEntity.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TestJavaEntity.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂join
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜TestJoinEntity.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜TestSingleEntity.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜TestSubJoinEntity.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TestSubSingleEntity.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂mapped_by
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜TestCountryEntity.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TestPeopleEntity.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜TestCountryEntityRepository.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜TestCountryEntityRepositoryCustom.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜TestPeopleEntityRepository.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TestPeopleEntityRepositoryCustom.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂repositoryImpl
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜TestCountryEntityRepositoryImpl.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TestPeopleEntityRepositoryImpl.java
┃ ┃ ┃ ┃ ┃ ┣ 📂util
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂mapstruct
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂mybatis
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂qualifier
┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂utils
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ObjectMapperUtil.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TestUtil.java
┃ ┃ ┃ ┃ ┃ ┗ 📜JpaTestApplication.java
┃ ┗ 📂resources
┃ ┃ ┣ 📂mapper
┃ ┃ ┣ 📂static
┃ ┃ ┗ 📜application.yml
┗ 📂test
┃ ┗ 📂java
┃ ┃ ┗ 📂com
┃ ┃ ┃ ┗ 📂example
┃ ┃ ┃ ┃ ┗ 📂jpa_test
┃ ┃ ┃ ┃ ┃ ┣ 📂tdd
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂controller
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TestControllerTest.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TestRepositoryTest.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂service
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TestServiceTest.java
┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TestIntegrationTest.java
┃ ┃ ┃ ┃ ┃ ┣ 📜JpaTestApplicationTests.java
┃ ┃ ┃ ┃ ┃ ┗ 📜README.md
```