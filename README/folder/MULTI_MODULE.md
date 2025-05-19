# SpringBoot-SideProject-Multi_Module
스프링 부트 멀티 모듈

## 기본 구조
- 모놀리식 + 클린 아키텍처 기반의 멀티 모듈, DDD 방법론 적용
  - `MA` 다른 서비스(=여기서는 다른 모듈을 의미) 직접 호출
    - `build.gradle` implementation project('module')
  - `Clean` interface(내부), impl(외부 - 구현체) 구조
    - `SRP` 각 모듈은 각자에 필수적인 의존성만 build.gradle 에 주입
  - `DDD` 단순하게 Entity 와 Repository 가 같은 모듈에 존재(domain)

## 설명
1. 아키텍처 스타일
   1. `Layered(레이어드) 아키텍처` 이름 그대로 계층을 나누는 설계 방식
      - 가장 흔하게 사용되는 방식으로, 특정 계층의 구성 요소는 해당 계층의 관련 기능만 수행해야 한다는 원칙
      - 일반적으로 Presentation, Business, Persistence, DataBase 의 4개 표준 레이어로 구성
        - 당연히 규모에 따라 병합하기도 하며, 그 이상의 레이어로 구성하기도 함
        - `Spring` Controller - Service - Domain(Entity) - Repository
        - `Architecture` Presentation - Business - Persistence - DataBase
   2. `Clean(클린) 아키텍처` Clean Architecture
      - SRP(단일 책임의 원칙) 를 준수하고, 의존성을 최소화하는 설계 방식
      - adapter(api), application(business), domain(entity), infra(db) 의 4개 표준 레이어로 구성
      - 레이어드 아키텍처와의 가장 큰 차이점은 각 레이어에 interface(내부), impl(외부) 형태로 설계
        - 외부 레이어가 바뀌더라도 구현체 내용만 변경될 뿐, 내부 레이어에는 영향이 없어야함
2. 방법론
   1. `CRUD` CRUD 기반 절차적 설계
      - `방법론 X` 특정 방법론이 전제가 되지 않고, Controller -> Service -> Repository 순서대로 코드를 구현
      - 일반적으로 RestAPI 형식으로 구현
   2. `TDD` 테스트 주도 개발
      - 테스트를 먼저 작성하고, 그에 맞춰 코드를 구현
      - Controller, Service, Repository 각각에 대해 테스트 클래스를 먼저 작성
   3. `DDD` 도메인 주도 설계
      - 도메인(Entity) 을 먼저 작성하고, 도메인 로직에 따라 분리하여 코드를 구현
3. 시스템 구조(배포)
   1. `MA(모놀리식) 아키텍처` Monolithic Architecture
      - 하나의 프로젝트에 모든 기능을 함께 포함하는 구조
      - `@Autowired` 내부 DI 를 통한 다른 서비스 직접 호출 
      - 단일 애플리케이션을 통해 실행 -> 내부 모듈은 나눌 수 있지만 실행/배포는 하나의 프로세스에서 동작
      - `SPOF(Single Point of Failure)` 한 번에 전체 배포되기에 서비스 중 하나라도 수정되면 전체 서비스에 영향
      - 계층(레이어) 기반
   2. `SOA 아키텍처` Service-Oriented Architecture
      - 통신 프로토콜을 통해 다른 프로젝트에 서비스를 제공하는 구조
      - `SOAP(XML) 방식` WSDL 또는 비동기 메시징(Kafka, RabbitMQ) 통신을 통해 다른 서비스 호출
      - 각 서비스를 분리하여 독립된 형태로 개별 배포를 진행 -> 느슨한 결합
      - 서비스 별로 독립 배포하기에 특정 서비스에 문제 생겨도 해당 서비스만 재배포 진행 가능
      - 기능(서비스) 기반
   3. `MSA 아키텍처`
      - 하나의 큰 시스템을 작은 독립적인 서비스들로 나눠서 운영하는 구조
      - `HTTP(JSON) 방식` RestTemplate 또는 Feign(FeignClient, RestClient) 통신을 통해 다른 서비스 호출
      -  SOA 와 거의 동일한 내용이지만, 통신 방식에서 상이
        - 반드시 HTTP 통신만은 아니고, GraphQL, gRPC, 비동기 메시징(Kafka, RabbitMQ) 등 상황에 따라 통신 방식 변경 가능
      - 기능(서비스) 기반

## 🖥️ Tip
1. 아키텍처 추세
   - `MA` -> `SOA` -> `MSA`
   - DDD 방법론을 접목하여 도메인 기반으로 넘어가는 추세
2. 아키텍처 비유 -> 건물
   - `스타일` 건물의 설계 방식
   - `방법론` 건물을 지을 때 따르는 철학이나 규칙
   - `구조` 건물이 실제로 하나인지, 여러 동인지, 어떤 식으로 나눠졌는지
3. 기타
   - `common` 공통 모듈 -> Nexus 나 Artifactory 에 배포
   - `yml` `config` 공통 설정 -> Spring Cloud Config 사용
   - `external-api` 외부 API 통신
   - `gateway` Spring Cloud Gateway
   - `discovery` Eureka
4. [출처] 참조
   - `멀티 모듈 사용 이유`
     - https://woo0doo.tistory.com/37
     - https://k9want.tistory.com/entry/Spring-스프링부트-프로젝트에-멀티모듈-설정하기
     - https://velog.io/@jthugg/spring-multi-module
   - `MA/MSA 비교` 
     - https://danmilife.tistory.com/38
     - https://fabric0de.tistory.com/19
     - https://dodo-devops.tistory.com/19
     - https://velog.io/@heoseungyeon/MSA-vs-모놀리식-akg64flw
   - `MSA(상세)` https://www.msap.ai/docs/msa-expert-from-concepts-to-practice/part-1-msa-fundamentals/ 
5. MSA 통신 방식 -> 불확실?? 추후 확인 필요
   - `도메인 내부 마이크로 서비스` API 기반의 Sync Call, 내부 메서드 호출, DB 통신 등을 수행
   - `도메인 외부 마이크로 서비스` Async Call(메세지 기반 통신) 등을 수행
6. [토이 프로젝트] 다른 사람 프로젝트
   - `상위 호환`
     - `blog` https://jaeseo0519.tistory.com/359
     - `git` https://github.com/CollaBu/pennyway-was
   - `참조`
     - `blog` https://soso-hyeon.tistory.com/83
     - `git` https://github.com/Team-Smeme/Smeme-server-renewal
   - `망나니개발자` 조금 예전인 대신에 상세함 -> 갓대희 유사
     - `blog` https://mangkyu.tistory.com/304
     - `git` https://github.com/MangKyu/multimodule-sample
     - `발표 자료` https://docs.google.com/presentation/d/1TSs-w9WW7Bz0qtu9byVR7UnHYNAYYWYt/edit?usp=sharing&ouid=115472032842511233289&rtpof=true&sd=true
   - `그 외`
     - `git` https://github.com/Gosrock/DuDoong-Backend
     - `git` https://github.com/TeamDilly/Packy-Server
     - `git` https://github.com/adjh54ir/blog-codes

---
## 예제
> 📘Module | 📗@SpringBootApplication | 📂Folder | 🧿build.gradle | 📕application.yml(미구현)

1. MA + Layered
- `@Autowired` 직접 호출
```bash
📘main
 ┣ 📂controller        → Controller + Swagger
 ┃ ┣ 📂service1
 ┃ ┣ 📂service2
 ┃ ┗ 📂service3
 ┣ 📂service           → Service
 ┃ ┣ 📂service1
 ┃ ┣ 📂service2
 ┃ ┗ 📂service3
 ┣ 📂domain            → Entity
 ┃ ┣ 📂service1
 ┃ ┣ 📂service2
 ┃ ┗ 📂service3
 ┣ 📂repository        → Repository(Mapper)
 ┃ ┣ 📂service1
 ┃ ┣ 📂service2
 ┃ ┗ 📂service3
 ┣ 📗Application.java
 ┗ 🧿build.gradle
```
2. MA + Clean
- `@Autowired` 다른 서비스(=여기서는 다른 모듈을 의미) 직접 호출
- `build.gradle` implementation project('module')
```bash
📘main
 ┣ 📘api               → Controller, ControllerDocs(Swagger)
 ┃ ┣ 📂service1
 ┃ ┣ 📂service2
 ┃ ┣ 📂service3
 ┃ ┣ 📗Application.java
 ┃ ┗ 🧿build.gradle
 ┣ 📘biz               → UseCase(Service), Service(ServiceImpl)
 ┃ ┣ 📂service1
 ┃ ┣ 📂service2
 ┃ ┣ 📂service3
 ┃ ┗ 🧿build.gradle
 ┣ 📘domain            → Entity
 ┃ ┣ 📂service1
 ┃ ┣ 📂service2
 ┃ ┣ 📂service3
 ┃ ┗ 🧿build.gradle
 ┣ 📘infra             → Repository, RepositoryImpl
 ┃ ┣ 📂service1
 ┃ ┣ 📂service2
 ┃ ┣ 📂service3
 ┃ ┗ 🧿build.gradle
 ┗📘common             → Common, Util
 ┃ ┗ 🧿build.gradle
```
3. MSA + DDD
- `infra` 각 서버의 Application 을 구동하고, infra 에서 통신을 통해 연결
- `common` jar 로 추출하여 별도 모듈로 의존성 등록
```bash
📘main
 ┣ 📘service1
 ┃ ┣ 📂api             → Controller, ControllerDocs
 ┃ ┣ 📂biz             → UseCase, Service
 ┃ ┣ 📂domain          → Entity, Repository, RepositoryImpl
 ┃ ┣ 📂infra           → RestClient
 ┃ ┣ 📗Application.java
 ┃ ┗ 🧿build.gradle
 ┣ 📘service2
 ┃ ┣ 📂api
 ┃ ┣ 📂biz
 ┃ ┣ 📂domain
 ┃ ┣ 📂infra
 ┃ ┣ 📗Application.java
 ┃ ┗ 🧿build.gradle
 ┣ 📘service3
 ┃ ┣ 📂api
 ┃ ┣ 📂biz
 ┃ ┣ 📂domain
 ┃ ┣ 📂infra
 ┃ ┣ 📗Application.java
 ┃ ┗ 🧿build.gradle
 ┗📘common
 ┃ ┗ 🧿build.gradle
 ┣ 📂.github
 ┃ ┣ 📂workflows
 ┃ ┃ ┗ 📕deploy.yml     → 배포 자동화를 위한 CI/CD 파이프라인 → GitHub Actions Workflows ⏩ 자동 배포
 ┗ 📕docker-compose.yml → 각 서비스 독립 실행을 위한 컨테이너 → Docker Compose ⏩ docker-compose up -d 명령으로 실행
```

---
## TODO:
1. `Clean` SRP 위반
    1. `api` HTTP 요청 역할(Controller), Service 실행 -> `biz` 만 의존성 주입
    2. `biz` 실질적인 업무 수행, Repository 실행 -> `domain` 만 의존성 주입
    3. `domain` ORM 을 통한 DB 연결, DB 작업 실행 -> 어느 의존성도 없어야함
    4. `common` 공통 작업 수행, 중복된 코드 제거, 어느 의존성도 없어야함
- `주관적인 생각`  
  해당 프로세스를 위해선 `biz` 에 Request, Response 를, `domain` 에 Entity 만을 진행해야하는데  
  QueryDSL 이 Projections 나 QResponse 를 이용한 `return Response repository.method(Request)` 으로 수행이 가능해서  
  어느 쪽이 더 비효율적인지 판단이 안됨.. (해당 로직 미사용 시, Service 작업 복잡도가 올라갈 수 있음)  
  이미 Projections 으로 정리를 마친 상태라 일단 기존 상태 유지하고, 추후 판단이 필요해보임..
  > `api` `biz` 둘 다 domain 의존성 주입, `domain` Request, Entity, Response 보관
2. `Security` `jwtUtil` SRP 위반
    1. `api` Security Filter
    2. `biz` UserDetailsService
    3. `domain` UserDetails Entity
- `주관적인 생각`
  일단 UserDetails 를 `@Entity` 로 왜 사용을 지양하는지 알거같음  
  하지만 감안하더라도 Security 관련한 의존성은 `api` 한 곳이어야 할듯한데 어떻게 분리해야할지 모르겠음..  
  `api` 에 security 폴더 및 로직을 모두 넣는건지, 아니면 `config` 모듈이 필요한건지 추후 판단이 필요해보임..
  > `api` `biz` `domain` 모두 Security 의존성 주입
3. `application.yml` 분리
    - profiles 에 따른 local(로컬), dev(개발), prod(운영) 분리 필요
    - 각 모듈별 yml 필요한지, 아니면 domain 만 profiles 에 따른 application-local.yml, ~-dev.yml, ~-prod.yml 로 분리 필요한지 추후 확인
   > `@SpringBootApplication` Application.java 이 실행되는 경로의 resources 만 인식되어, `api` 모듈의 application.yml 에 모든 내용 저장
4. `CI/CD` 적용
    1. `CI` docker-compose.yml → Docker Compose
    2. `CD` deploy.yml → GitHub Actions

---
## Dummy
- SOA
```bash
📦soa-project
┣ 📂service-contract  → WSDL, Interface 정의
┣ 📂service-impl      → 실제 비즈니스 로직 구현
┣ 📂adapter           → 메시지/웹서비스 통신 처리
┣ 📂shared            → 공통 DTO, 유틸
┗ 📂config            → 통합 설정
```

- 이전 프로젝트 구조(Mybatis)
> @RestController → interface service → @Service → interface repository → @Repository → interface @ReadDatabase/@WriteDatabase → resource/mapper.xml  
> api(in - controller) → biz(in - port) → biz(service) → biz(out - port) → api(out - persistence) → api(out - persistence)  

```bash
📘main
 ┣ 📂adapter 
 ┃ ┣ 📂in
 ┃ ┃ ┗ 📂controller
 ┃ ┃ ┣ 📂out
 ┃ ┃ ┃ ┗ 📂persistence
 ┃ ┃ ┃ ┃ ┣ 📂adapter    → @Repository 구현체
 ┃ ┃ ┃ ┃ ┣ 📂entity     → @Entity
 ┃ ┃ ┃ ┃ ┗ 📂repository → interface @ReadDatabase/@WriteDatabase → mybatis mapper.xml 연결용
 ┣ 📂application
 ┃ ┣ 📂persistence
 ┃ ┃ ┣ 📂service        → @Service 구현체
 ┃ ┃ ┣ 📂port
 ┃ ┃ ┃ ┣ 📂in
 ┃ ┃ ┃ ┃ ┗ 📂port       → interface Service
 ┃ ┃ ┃ ┣ 📂out
 ┃ ┃ ┃ ┃ ┗ 📂port       → interface Repository
 ┃ ┃ ┗ 📂response
 ┣ 📂domain
 ┃ ┣ 📂request
 ┃ ┗ 📂response         → interface @Mapper → mapstruct 생성용
 ┣ 📂mapper
 ┃ ┗ 📂response
 ┣ 📗Application.java
 ┣ 🧿build.gradle
 ┣ 📂resource
 ┃ ┣ 📂mapper
 ┃ ┃ ┣ 📂read           → 📕mapper.xml(select)
 ┃ ┃ ┗ 📂write          → 📕mapper.xml(DML) → select sequence, fn() 포함
```
