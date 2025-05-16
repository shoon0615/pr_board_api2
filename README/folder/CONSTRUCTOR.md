# SpringBoot-SideProject-Request
스프링 부트 요청

## 기본 구조
- **@RequestMapping**
1. `@GetMapping`
   1. `@NoArgsConstructor` 기본 생성자 리플렉션
      - access 나 private 무시하고 사용 가능(단, 존재는 해야함)
   2. `@ModelAttribute` 생략 시 기본 설정
      - @Setter 만 적용 -> @Getter 없어도 Field 에 입력
2. `@PostMapping`
   1. `@NoArgsConstructor` 기본 생성자 리플렉션
   2. `@RequestBody` 생략 불가
      - `@Getter` 존재하는 필드만 대상으로 판단
        - `Field` 필드 리플렉션 -> @Getter 로 판단만 하고, 메소드가 사용되진 않음
      - `@Setter` 존재할 경우, @Setter 로 @Override

- **@RequestParam**
1. `@RequestParam` 단일값 자동 매핑 -> Map 포함
    - `UrlMapping` QueryString
    - `@Getter` 리플렉션
2. `@ModelAttribute` DTO 자동 매핑
    - `UrlMapping` QueryString
    - `@Setter` 리플렉션
3. `@RequestBody` DTO 자동 매핑
    - `body` JSON
    - `Field` 리플렉션

---

## 사용 객체
- **@GetMapping**
1. `SearchCondition` 조회용 DTO -> 검색 조건
```java
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class SearchCondition extends PageCondition {}
```
```java
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder
public class SearchCondition extends PageCondition {}
```
> - `상단` 단순화 형태, `하단` 정석 형태
> - `@SuperBuilder` 테스트 및 직접 사용 시, Builder 패턴만을 이용하여 객체 생성

2. `PageCondition` 페이징 DTO
```java
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class PageCondition {}
```
> - `@PageableDefault` 페이징 편의성 제공 라이브러리 -> `@Valid` 적용 불가 및 모듈화가 안되어 **미사용**

- **@PostMapping**
1. `Request` 저장용 DTO
```java
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class Request {}
```
```java
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Request {}
```
> - `상단` 단순화 형태, `하단` 정석 형태  
> - 공통성 및 단순화를 위해 형식 통일

---

## @Builder
1. 기본
```java
public class Request {}
```
> `public` Request request = new Request();  

2. @Builder
```java
@Builder
public class Request {
    private String column1;
    private String column2;
    
    @Builder
    private Request(String column2) {
        // super(column3, column4);
        this.column2 = column2;
    }
}
```
> `public` Request request = Request.builder().column1("column1").column2("column2").build();  
> `protected` Request request = new Request("column1", "column2");  
> `public` Request request = Request.builder().column2("column2").build();  
> `private` Request request = new Request("column2");  
> - `Class` 전체 생성자 자동 생성
>   - `자동 생성` @AllArgsConstructor(access = AccessLevel.PROTECTED)
>   - @Builder 만 사용 시, 임의 생성자 선언과 동일하게 기본 생성자 제거됨
> - `Method` 일부 필드 생성자 선언  
>   - method 의 접근 제한자로 생성자 생성

3. 기본 + @Builder
```java
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Request {}
```
> `public` Request request = Request.builder().column("column").build();  
> `private` Request request = new Request();  
> `private` Request request = new Request("column");  
> - `Class` Builder 생성용 생성자 필요
>   - 임의 생성자가 존재하기에 자동 생성이 발생하지 않아 @AllArgsConstructor 필요
> - 직접 생성 방지를 위해 access = AccessLevel.PRIVATE 선언

4. @SuperBuilder
```java
@SuperBuilder
public class Request extends ParentRequest {}

@SuperBuilder
public class ParentRequest {}
```
> `public` Request request = Request.builder().column("column").parent("parent").build();  
> `protected` Request request = new Request(Request.RequestBuilder)  
> - `Class` Builder 생성자 자동 생성 -> 기본 생성자 제거됨, @Builder 와 달리 생성자 없음
> - `자동생성` protected Request(Request.RequestBuilder<?, ?> b)
> - Builder 생성자가 필요하기에 extends 한 부모 클래스에도 @SuperBuilder 선언 필요

5. 기본 + @SuperBuilder
```java
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder
public class Request extends ParentRequest {}

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class ParentRequest {}
```
> `public` Request request = Request.builder().column("column").parent("parent").build();  
> `protected` Request request = new Request(Request.RequestBuilder);  
> `private` Request request = new Request();  
> - 생성자 리플렉션 시, 같은 생성자가 필요하기에 extends 한 부모 클래스에도 같은 생성자 선언 필요
>   - `@NoArgsConstructor` `@RequiredArgsConstructor` `@AllArgsConstructor`
>   - 자식 클래스의 access 는 상관없지만, 부모 클래스는 자식 클래스에서 사용을 위해 `PRIVATE` 불가(`PROTECTED` 가능)

---

## 🖥️ Tip
- `불변성(Immutable)` 처음에 값을 지정받고, 이후 변경 불가
   - 직접 생성하지 않고, 전달받은 객체는 최대한 불변성 유지
   - 어느 시점, 어느 객체에서 변경을 진행했는지 확인이 어려움
   - `Class` @Setter 의 사용을 지양하고, 필요한 부분만 메소드로 제공
   - `final` 전달받은 객체에 final 로 지정
     - String, int 같은 단일 타입은 수정 불가하지만, Map, DTO 같은 복합 타입은 final 설정해도 수정 가능
     - 명시적 의미를 갖기 위해 복합 타입이어도 전달받은 객체는 final 설정
     - `final class` 상속 불가 설정 -> 상속하지 말라는 의도 명확
   - `@NoArgsConstructor(access = AccessLevel.PRIVATE)` 무분별한 생성을 방지하기 위해 기본 생성자 접근 제한 설정
   - `@UtilityClass` Util 편의성 제공 라이브러리
     - `@NoArgsConstructor(access = AccessLevel.PRIVATE)` `final class` 탑재
     - class 및 모든 필드/메소드가 자동으로 `static` 처리
- `Record`
   - 불변성을 위해 Request 에 final 필드 및 @RequiredArgsConstructor 을 사용하고 싶어도  
      Spring 은 기본 생성자 + 리플렉션을 기반으로 동작 및 @ModelAttribute 는 @Setter 까지 사용하기에  
      Request 는 불변성 적용을 할 수 없었으나 Java17 부터는 불변성 적용이 가능한 Record 객체가 도입됨
   - class 및 모든 필드는 자동으로 `final` 처리
   - implements 가능, extends 불가
   - `@Data` @Setter 를 제외한 나머지 기능 탑재
   - ### **TODO:** 공식 문서에 RequestDTO 용으로 계획된 객체기에 추후 사용 요망