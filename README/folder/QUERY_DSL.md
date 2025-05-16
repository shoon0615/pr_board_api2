# SpringBoot-SideProject-QueryDSL
스프링 부트 QueryDSL

## 설명
1. `MyBatis`
   - 자연어(Native Query) 형태로 SQL 문법을 그대로 사용하는 ORM 방식
   - 가독성이 좋고, Java 와 상관없이 동작하기에 SQL 동작 규칙만 준수하면 되기에 간단함
   - 다만, Java 의 대표적 특징인 객체지향, 컴파일(타입 안정성) 체크 등의 방식도 적용되지 않음
2. `JPQL(Java Persistence Query Language)`
   - JPA 에서 사용하는 Java 전용으로 특화된 객체지향 쿼리 언어
   - Oracle, PostgreSQL, MySQL 같이 Java 에서 사용하는 SQL 방식
   - ORM 이 Java <-> DB 의 네트워크 연결이라면, JPQL 은 Java <-> DB 간 SQL 연결(실질적으론 ANSI-SQL 로 작동함) 
3. `QueryDSL`
   - JPQL 을 안전하게 작성하도록 도와주는 도구
   - `타입 안정성(Type-safe)` 미리 QClass 를 생성하기에 컴파일 시점에서 검증 가능
   - `가독성` @Query 와 달리 문자열이 아닌 코드로 작성
   - `재사용성/객체지향` 코드로 구성되기에 동적으로 작성 및 분리 가능

## JPQL 방식
1. `JpaRepository` 기본적으로 제공하는 JPQL 방식
   - findById, findAll, save, saveAll, delete, deleteAll...
2. `Named Query` JPA 의 명명규칙을 통한 JPQL 방식
   - @Query
3. `Native Query` JPA 의 규칙을 적용하지 않는 직접 작성한 SQL 방식(Dynamic Query)
   - @Query(nativeQuery = true)
4. `QueryDSL` JPQL 을 안전하게 작성하도록 도와주는 도구
   - JPAQueryFactory
5. `EntityManager` 수동적으로 적용하는 JPQL 방식
   - persist, flush, clear

## 서버 실행 단계
1. `컴파일 시점(Compile-time)` 코드 작성
   - JpaRepository, QueryDSL
2. `초기화 시점(Initialization-time)` 서버 실행 중 
   - SpringContext 초기화 시점 -> Bean 등록, DI(의존성 주입), 쿼리 파싱, static, 리플렉션 기반 분석 등..
   - Named Query
3. `실행 시점(Run-time)` 서버 실행 이후, 메소드 실행
   - Native Query

## N+1
- **설명**
1. JPA 사용 시 자주 발생하는 비효율적인 쿼리 발생 문제
2. hibernate - JPA 와 Spring Data JPA 의 방식 차이로 발생
   1. `hibernate - JPA` EntityManager 를 통한 과정(persist -> flush -> clear)
      - findById
   2. `Spring Data JPA` JpaRepository 메소드(flush -> clear)
      - findAll 을 포함한 그 외 모든 JPQL
3. JpaRepository 는 persist 없이 진행되어 사실상 fetchType 이 의미없어짐
   - `EAGER` 한번에 모든 Entity 의 모든 정보 호출
   - `LAZY` 조회에 필요한 Entity 정보만 호출
   - 해당 내용으로 진행되어야하지만, EAGER 여도 LAZY 처럼 조회 -> 한번에 join 이 아닌 한번에 여러 단계의 sql 이 실행됨

- **문제점**
1. `성능 저하` 쿼리가 N+1개 실행되어 DB 요청 횟수가 증가하고, 응답이 느려짐
2. `트래픽 증가` 네트워크 비용 증가, DB 부하
3. `캐시 효과 감소` 동일한 연관관계일 경우에도 매번 요청

- **해결법**
- LAZY 여도 조회하면 EAGER 처럼 조회 -> 한번에 join 하여 모든 정보 호출
1. `Fetch Join` 사용자 임의 구문으로 정보를 미리 가져와 해결 -> N+1 문제를 방지하여 최적화
   - 실제 SQL 에 fetch join 이 존재하는 것이 아니라, JPQL -> ORM -> SQL 로 넘어가며 변환됨
   - `@Query` Entity 반환
   - `QueryDSL` ResponseDTO 반환
2. `@EntityGraph` Outer Join 구문으로 정보를 미리 가져와 해결 -> distinct 자동 적용 안됨
3. `@BatchSize` Where ~ In 구문으로 정보를 미리 가져와 해결 -> between 이 아니라 모든 인자 들어감
   - `@BatchSize` Entity 또는 연관관계 Entity 에 어노테이션 작성
   - `spring.jpa.properties.hibernate.default_batch_fetch_size` yml 설정
4. `@Fetch(FetchMode.SUBSET)` 성능이 가장 떨어지고, 복잡
5. `@Query(nativeQuery = true)` JPQL 로 적용 안되기에 연관 없음

- **예제(Fetch Join)**
```java
@Entity
public class ChildEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private ParentEntity parents;
}
```
```java
@Entity
public class ParentEntity {
    @OneToMany(mappedBy = "parents")
    private List<ChildEntity> childs = new ArrayList<>();
}
```
1. 일반 조회(LAZY)
   - `ChildEntity.getParents()` `ParentEntity.getChilds()`
   1. `findById` 본인 + 연관관계 수만큼 SQL 쿼리 생성(1+1)
      1. SELECT c FROM ChildEntity c WHERE c.id = ?
      2. SELECT p FROM ParentEntity p WHERE p.id = ?
   2. `findAll` 본인 + 연관관계 데이터만큼 SQL 쿼리 생성(1+N)
      - 1번에서 @JoinColumn 의 NULL 을 제외한 데이터 수만큼 실행 -> c.parents_id 개수만큼 실행
      1. SELECT c FROM ChildEntity c
      2. SELECT p FROM ParentEntity p WHERE p.id = 1
         - p.id = c.parents_id
      3. SELECT p FROM ParentEntity p WHERE p.id = 2
      4. SELECT p FROM ParentEntity p WHERE p.id = 3
2. JPQL(Fetch Join)
   1. `@ManyToOne` ChildEntity.getParents()
      1. SELECT c, p FROM ChildEntity c JOIN ParentEntity p ON p.id = c.parents_id WHERE p.id = ?
      2. SELECT c, p FROM ChildEntity c JOIN ParentEntity p ON p.id = c.parents_id
   2. `@OneToMany` ParentEntity.getChilds()
      1. SELECT DISTINCT p, c FROM ParentEntity p LEFT JOIN ChildEntity c ON p.id = c.parents_id

- **Tip**
1. ? 변수를 통한 쿼리를 등록/캐싱한다는 느낌으로 이해
   1. `LAZY` 요청한 파트만 SQL 쿼리 조회/등록
   2. `JPQL` 요청한 파트 + 연관관계까지 한번에 SQL 쿼리 조회/등록 
2. 등록한 쿼리에서 정보를 가져올 수 있는 경우, 새 쿼리를 실행하는 것이 아닌 캐싱된 쿼리 정보에서 조회
3. 정보를 가져오지 못할 경우, 새 쿼리를 통해 정보 캐싱(등록)

## Fetch Join
- Entity 자체를 직접 호출하는 경우에만 사용
1. `@~ToOne` 엔티티 페치 조인(Join)
   - 제약 없음
2. `@~ToMany` 컬렉션 페치 조인(Left Join)
   - JPA 는 하나의 컬렉션(List)만 Fetch Join 가능
   - 카테시안 곱(Cartesian product)이 발생하므로 distinct 또는 Set 사용 필수
   - `MultipleBagFetchException` 다중 Fetch Join 진행 시, 오류 발생
     - 하나에만 하고, 나머지는 지연 로딩 그대로 하거나 @BatchSize 사용
   - QueryDSL 은 JPQL 을 안전하게 작성하도록 도와주는 도구일 뿐, 실제로는 여전히 JPA 의 제약을 그대로 따름
     - 다중 Fetch Join 진행 시, @Query 는 초기화 시점, QueryDSL 은 런타임 시점에 같은 오류 발생
3. `실무`
   - `@~ToOne` Fetch Join
   - `@~ToMany` Batch Size
     - List<> findAll() 대신 Page<> findAll(Pageable) 적용

---

## 🖥️ Tip
1. Fetch Join, Batch Size 등의 JPQL 방식은 엔티티 객체를 로딩(JPA 내부에서 proxy 제거 및 초기화) 하기 위한 것이지,  
   ResponseDTO 로 반환(projection) 을 하는 경우에는 필요하지 않음  
2. 사이드 프로젝트에서는 모두 ResponseDTO 로 반환할 것이기에 사실상 N+1 및 위의 내용 모두 몰라도 됨  
3. `@ToString` @Override ObjectMapper.writeValueAsString(this)
   - Entity 에 대해 toString() 사용 시, 전체 직렬화되어 조회 진행 
   - 본인 Entity 만 조회해도 연관관계 Entity 까지 모두 조회되어, 사실상 상시 Eager 사용과 유사

## 기본 구조
- **기본**
1. `@Entity` 어노테이션이 붙은 class 는 컴파일 완료 시, 자동으로 QClass 생성
2. `JPAQueryFactory`
   1. `Entity`
      1. `select(QClass)`
      2. `selectFrom(QClass)`
   2. `Etc`
      1. `select(QClass.column1)` [하나] 해당 컬럼 타입 반환
         - ex: String
      2. `select(QClass.column1, QClass.column2)` [둘 이상] Tuple 반환
   3. `ResponseDTO`
      1. `Projections.constructor` 생성자 기반
      2. `Projections.fields` 필드 기반
         - as 매핑 가능
      3. `Projections.bean` @Setter 기반
         - as 매핑 가능
      4. `@QueryProjection` QResponse 기반
3. `@Repository` Impl 접미사 네이밍 규칙으로 인해 파일 명칭이 JpaRepository + Impl 인 경우, Spring Data JPA 가 @Repository 없이도 자동으로 주입
   - `@Repository` Impl 이 아닌 경우, 수동 주입
   - `@NoRepositoryBean` DI(의존성 주입) 차단
   - [출처] https://hstory0208.tistory.com/entry/QueryDSL-Repository-생성하기-사용자-정의-리포지토리