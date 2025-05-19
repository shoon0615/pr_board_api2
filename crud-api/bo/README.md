# Application(Back Office)
Controller, Swagger, Security

DDD 원칙 위배 => 관심사 분리(각 기능은 서로의 영향을 안받고 본인 역할만 충실해야함 -> 의존성 없어야함)
api -> biz
biz -> domain
domain -> `None`

- Common 은 별개
common -> `None`

## 현재
api -> biz, domain
biz -> domain
domain -> `None`

## 해결방안??
1. api 에 biz 포함
```
api -> domain
domain -> `None`
```
2. domain 의 queryDSL 때문인데 req, res 모두 biz 에서 entity 로 변경하여 전달/반환하게 수정
   - req, res 는 biz 로 이동?? -> biz - port(in/out)?? 
3.

---

# 설명회(20250517)
1. `Proxy 영속성` update, Lazy
2. `fetch join` findAll, em(단일), Lazy -> N+1 방지
3. `Soft Delete` `cascade` `Native Query`
4. `Named Query` -> `JPQL` -> `QueryDSL` 조회 위주, 작업은 X(batch || nativeQuery || mybatis)
5. `QueryDSL` -> `DTO(req, res)` 멀티 모듈 영향, Inner Class
6. `BaseEntity` @Id, deletedAt(LocalDate), @CreatedBy(UUID), @LastModifiedBy, @CreatedDate, @LastModifiedDate
7. `QClass` `QResponse` `Mapstruct` 강결합
8. `리플렉션` Reflection
   1. `ObjectMapper` new() -> Proxy 영속성 미적용
      - Class<?> `getClass()` getDeclaringClass(), getDeclaredFields(), getDeclaredMethods()
      - getClass().getDeclaredField().setAccessible(true) -> private, access 등 접근 제한자 무시
   2. `BeanMapper` this -> Proxy 영속성 적용
   3. `from/toEntity`
9. `TDD` -> Controller, Service, Repository -> api, biz, domain
10. `Security` `Web`: Session, `Mobile`: Token
