# SpringBoot-SideProject-Reflection
스프링 부트 리플렉션

## 기본 구조
- **설명**
1. `직렬화(Serialize)` 데이터 포맷 변환
2. `리플렉션(Reflection)` 클래스의 구조를 가져와 필요한 정보(생성자, 필드, 메서드) 를 사용할 수 있는 기술
3. `@ToString` 직관적인 문자열을 반환하는 디버깅 용도의 메소드

- **대표적 예시**
1. `@RequestMapping` 런타임 리플렉션
    - `@RequestParam` @Getter
    - `@ModelAttribute` @Setter
    - `@RequestBody` @Setter -> Field
2. `ObjectMapper` 직렬화 -> 런타임 리플렉션
   - `writeValueAsString` @Getter
   - `readValue` @Setter
   - `convertValue` @Getter
3. `@ToString` 컴파일 리플렉션(직렬화 X)
    - `@ToString` @Getter -> Field
    - `@ToString(doNotUseGetters=true)` Field
    - `ReflectionToStringBuilder` Field
4. `MapStruct` 컴파일 시점에 변환용 매핑 class 생성(=QClass 유사)
    - `@Mapper` interface Mapper extends GenericMapper<Request, Entity, Response>

- **사용자 기준**  
 : 필드 직접 접근은 기본적으로 비활성화  
`private` 필드 생성(캡슐화 위반 방지 및 보안성 향상)  
또한 객체가 노출시키고자 하는 값을 그대로 조회할 수 있기에 메소드 기반 조회 방식 사용(@Getter, 지연 로딩 등..)

---

## 심화
1. `리플렉션`
   - Class<?> `getClass()` getDeclaringClass(), getDeclaredFields(), getDeclaredMethods()
   - 컴파일 시점에 사용 또는 확인할 수 없더라도 런타임 시점에 가능해짐(ex: Controller - Request 에 기본 생성자 없으면 에러 발생)
   - `setAccessible(true)` access 나 private 무시하고 사용 가능 -> Spring 에서 true 가 기본 설정
   - 의존성 주입(DI), ORM 등 Spring 대부분의 설정이 해당 방식으로 처리
     - 별도의 코드를 생성하지 않고, 런타임에 클래스의 필드/메소드를 리플렉션하여 구현
2. `직렬화`
   - `ObjectMapper` 내부적 또는 직접 선언하여 사용
   - `serialization` **[직렬화]** DTO -> JSON
     - `writeValueAsString()` 변환된 String 확인 가능
     - `@Getter` 필수
     - `@JsonIgnore` JSON 관련 어노테이션에 따라 무시 가능(=포함시키지 않을 수 있음)
   - `deserialization` **[역직렬화]** JSON -> DTO
     - `readValue()` 내부적으로 리플렉션 적용되기에 access 상관 없음
     - `@NoArgsConstructor` `@Setter` 필수
     - `@JsonIgnore` JSON 관련 어노테이션에 따라 무시 가능
3. `@ToString`
   - `우선순위` @ToString > @Override > 미선언(기본: 객체만 출력, 필드는 출력 안됨)
   - 단순 디버깅 확인용 메소드에 컴파일 시점에 미리 코드를 생성하기에 성능 부하 없음
   - @Getter -> Field 리플렉션 순으로 확인하여 toString() 반환
   - `@ToString`
     - `(doNotUseGetters = true)` @Getter 확인 안하고, 바로 Field 리플렉션 확인으로 넘어감
     - `(callSuper = true)` Extends 한 객체의 컬럼까지 출력
     - `(exclude = {})` toString 목록에서 제외
     - `@JsonIgnore` JSON 관련 어노테이션 영향 없음 -> 직렬화 미사용
   - `ReflectionToStringBuilder` toString 편의성 제공 라이브러리
     - `Field` @Getter 확인 안하고, 바로 Field 확인
     - `setExcludeFieldNames()` 민감하거나 순환 참조될 수 있는 필드는 toString 목록에서 제외

---
## ToString
1. `우선순위` @ToString > @Override > 미선언
2. `기본` 미선언, 객체만 출력, 필드는 출력 안됨
3. `@ToString` SHORT_PREFIX_STYLE 유사, 객체가 필드면 객체만 출력됨
4. `@Override`
   1. `ReflectionToStringBuilder` setExcludeFieldNames() 를 통해 개별 @Override 설정
       - ToString() 은 직렬화를 사용하지 않아 @JsonIgnore 처리 안되어 순환참조 필드마다 exclude 필요
      1. `SHORT_PREFIX_STYLE` JAVA 형식, 1줄 출력
      2. `MULTI_LINE_STYLE` JAVA 형식, 여러줄 출력
      3. `JSON_STYLE` JSON 형식, 1줄 출력
   2. `ObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString()` @JsonIgnore 를 통해 공통 @Override 설정  
      - this 가 존재할 경우, 순환참조 발생 -> JSON 관련 어노테이션으로 한쪽만 직렬화 처리하거나 역직렬화 방지
        - 필드가 아니라 @Getter 리플렉션이므로 필드 없이 메소드만 존재해도 직렬화 처리됨 
      1. `MULTI_JSON_STYLE` JSON 형식, 여러줄 출력
      2. `@JsonIgnore`
         - 순환참조되는 양측 중 한쪽에 작성 -> 일반적으로 @OneToMany 에 작성
      3. `@JsonManagedReference` `@JsonBackReference`
         - @JsonManagedReference 은 그대로, @JsonBackReference 에 @JsonIgnore 처리와 같은 효과 -> 일반적으로 @OneToMany 에 작성
      4. `@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")`
         - 순환참조되는 양측 중 한쪽에 작성 -> 일반적으로 @ManyToOne 에 작성
         - 역직렬화 시, 객체 대신 property(id) 만 출력되게 설정
           - 치명적 단점: @ManyToOne 측은 1건이라 정상출력되나, @OneToMany 측은 다건이라 1개 이상 변환안됨
      5. `@JsonIgnoreProperties("column")`
         - 순환참조되는 양측 모두에 작성 -> 역직렬화 방지

---

## 🖥️ Tip
- `런타임 리플렉션` 런타임 시점에 리플렉션 진행
    - `장점` ↑
      - `약결합` 따로 생성되는 class 없음
      - `유연성` 확장, 수정해도 추가 수정 없음
    - `단점` ↓
      - `성능` 매 사용 시점마다 진행
      - `안전성` 런타임 시에 체크
      - `보안` 접근 제한자 무시 가능
    - `ObjectMapper` 편의성 ↑, 성능 ↓
      - 매 사용 시점마다 getXxx 메소드를 하나하나 호출해 반환값을 모으기에 성능 저하 이슈
- `컴파일 리플렉션` 컴파일 시점에 리플렉션 진행하거나 클래스 미리 생성
    - `장점` ↑
      - `성능` 미리 컴파일되어 속도 향상
      - `안전성` 컴파일 시점에 타입, 오류 체크
      - `보안` 접근 제한자로 제한 가능
    - `단점` ↓
      - `강결합` 서비스별 class 생성 필요, class 생성 실패 또는 문제 발생 시 실행 불가
      - `유연성` 확장, 수정 시마다 관련된 모든 class 에 작업 필요
    - `@ToString`
    - `@Entity` querydsl 사용 시 QEntity 생성
    - `@QueryProjection` querydsl 사용 시 QResponse 생성
    - `@Mapper` mapstruct 사용 시 MapperImpl 생성
