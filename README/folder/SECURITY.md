# SpringBoot-SideProject-SpringSecurity
스프링 부트 Spring Security

## 기본 구조
- **로그인(ID/PW)**  
1. Security 설정에 따라 Filter 자동 적용
2. [인증 체크] `Filter` 미인증 객체 생성
3. [인증 연결] `Manager` Filter 에 적합한 Provider 자동 연결
4. [인증 작업] `Provider` 미인증 객체와 DB 사용자 정보 비교하여 인증 객체 전환
5. [인증 성공] 
   - `Session` : `Server` `Session` 에 객체 저장
   - `Stateless` : `Client` Header 에 사용될 `Cookie` || `Token` 반환

---

- **인증(Authenticated)**
1. Security 설정에 따라 Filter 자동 적용
2. [인증 체크] `Filter`
   - `Session` : `Server` `Session` 에 저장(캐싱) 정보 확인 
   - `Stateless` : `Client` Header 의 `Cookie` || `Token` 정보 확인
3. [검증 연결] `Manager`
4. [검증 작업] `Provider`
   - `Session` : 만료(Timeout) 여부 정도만 확인 
   - `Stateless` : 만료(exp) 여부 + 저장된 정보와 DB 사용자 정보 비교하여 인증 객체 반환
     - 모든 요청마다 DB 비교를 통한 검증 작업 시, 서버에 부담 + Stateful 하기에 `Redis` 에 캐싱
5. [인증 성공] 인증 성공 기준은 `Server` `SecurityContext` 의 인증 객체 확인
   - `Session` : `Server` `SecurityContext` 에 객체 저장(`Session` 정보)
   - `Stateless` : `Server` `SecurityContext` 에 객체 저장(인증 객체 정보)

---

## 기본 구조(실제 로직)

![image-1.png](README-img%2Fimage-1.png)
[출처](https://bestdevelop-lab.tistory.com/category/Language/Spring%20Security)

1. `Servlet Container` DelegatingFilterProxy(WAS)
   - SecurityFilterChain 호출
2. `Spring Container` FilterChainProxy
   - SecurityFilterChain 탐색
3. `SecurityFilterChain` @EnableWebSecurity
   - HttpSecurity 메소드에 따라 적합한 Filter 실행
4. `AuthenticationFilter` doFilter() { AuthenticationManager.authenticate(Authentication) }
   - `Authentication` UsernamePasswordAuthenticationToken.unauthenticated(id, pw) 생성
   - 미인증 Authentication 객체(id, pw) 를 AuthenticationManager 에 전달
5. `AuthenticationManager` AuthenticationProvider.authenticate(Authentication)
   - Filter 에 적합한 Provider 실행
6. `AuthenticationProvider`
   - `UserDetails` retrieveUser(id) { UserDetailsService.loadUserByUsername(id) }
   - additionalAuthenticationChecks(UserDetails, Authentication) { passwordEncoder.matches(UserDetails.pw(암호화), Authentication.pw(평문)) }
   - `Authentication` createSuccessAuthentication(UserDetails, Authentication) { UsernamePasswordAuthenticationToken.authenticated(principal, credentials, authorities) }
      - `principal`: UserDetails, `credentials`: Authentication.pw, `authorities`: UserDetails.authorities
   - 미인증 Authentication 객체에 인증 작업을 수행해 인증 Authentication 로 전환
   - 미인증 Authentication(id, pw) 객체(id) 로 DB 에서 사용자 정보를 가져온 후, DB(pw) <-> 객체(pw) 의 비밀번호가 일치하면  
     인증 Authentication(UserDetails) 객체 생성 후 반환
7. `UserDetailsService` loadUserByUsername(id) { Repository.findById(id) }
   - `UserDetails` DB 에서 id 를 기반으로 사용자 정보 호출(id, pw, role)
8. `UserDetails` username(id), password(pw), authorities(role)
   - `User` implements UserDetails

- **성공**
1. `AuthenticationFilter` successfulAuthentication(HttpServletRequest, HttpServletResponse, FilterChain, Authentication)
   - SecurityContext.setAuthentication(Authentication)
   - `AuthenticationSuccessHandler` onAuthenticationSuccess { sendRedirect(request, response) }
   - ~~FilterChain.doFilter()~~
2. `SecurityFilterChain` authorizeHttpRequests(request.requestMatchers(patterns))
   - `AuthenticationEntryPoint` SecurityContext.getAuthentication()
      - `401 Unauthorized` 인증 여부 확인(authenticated)
   - `AccessDeniedHandler` Authentication.authorities
      - `403 Forbidden` 권한 여부 확인(hasRole, hasAuthority)
3. `Controller` `Service` `Repository`
   - `UserDetails` SecurityContext.getAuthentication().getPrincipal()
      - login 사용자 정보 호출

- **실패**
1. `AuthenticationFilter` unsuccessfulAuthentication(HttpServletRequest, HttpServletResponse, AuthenticationException)
   - `AuthenticationFailureHandler` onAuthenticationFailure { sendRedirect(request, response) }

---

### formLogin(Session)
1. `SecurityFilterChain` formLogin
2. `AuthenticationFilter` UsernamePasswordAuthenticationFilter, AbstractAuthenticationProcessingFilter
3. `AuthenticationManager` ProviderManager
4. `AuthenticationProvider` DaoAuthenticationProvider, AbstractUserDetailsAuthenticationProvider
   - Provider 는 `UserDetailsService`, `PasswordEncoder` 필수
   - `@Service` `UserDetailsService`
   - `@Bean` `BCryptPasswordEncoder`
5. `AuthenticationSuccessHandler` SavedRequestAwareAuthenticationSuccessHandler, SimpleUrlAuthenticationSuccessHandler
6. `AuthenticationFailureHandler` SimpleUrlAuthenticationFailureHandler
7. `SecurityFilterChain` authorizeHttpRequests(request.requestMatchers(patterns))
   - `AuthenticationEntryPoint` `AccessDeniedHandler`

---

### JWT Token(Login)
1. `SecurityFilterChain` addFilterBefore(new CustomFilter)
2. `AuthenticationFilter` CustomFilter
3. `AuthenticationManager` ProviderManager
   - `AuthenticationProvider` @Bean 등록 시 CustomProvider 대체 가능
   - `@Bean` authenticationManager() { new ProviderManager(new CustomProvider) }
4. `AuthenticationProvider` AbstractUserDetailsAuthenticationProvider, DaoAuthenticationProvider
   - Provider 는 `UserDetailsService`, `PasswordEncoder` 필수
5. `AuthenticationFilter` `@Override` successfulAuthentication
   - `Token` Jwts.claims().signWith(SecretKey)
      - `UserDetail` id, authorities 등 민감하지 않은 정보만 Token 에 저장 후 반환
   - ~~`AuthenticationSuccessHandler`~~
6. `AuthenticationFilter` `@Override` unsuccessfulAuthentication
   - ~~`AuthenticationFailureHandler`~~

### JWT Token(Authenticated)
1. `SecurityFilterChain` addFilterBefore(new CustomFilter)
2. `AuthenticationFilter` HttpServletRequest.getHeader(Authorization)
3. `AuthenticationProvider` SecurityContext.setAuthentication(Authentication)
   - `Claims` Jwts.parser().verifyWith(SecretKey).parseSignedClaims(Token).getPayload()
   - `UserDetailsService` loadUserByUsername(claims.getSubject())
   - `Authentication` UsernamePasswordAuthenticationToken.authenticated(userDetails, null, userDetails.authorities)
   - Token 의 id 로 DB 에서 사용자 정보를 가져온 후, 인증 Authentication(UserDetails) 객체를 생성해 Context 저장
   - CustomProvider 가 없을 경우, `AuthenticationFilter` 에서 수행(=id, pw 를 안받기에 굳이 필요하지 않을 수 있음)
   - 모든 요청마다 DB 비교를 통한 검증 작업 시, 서버에 부담 + Stateful 하기에 `Redis` 에 캐싱
4. `SecurityFilterChain` authorizeHttpRequests(request.requestMatchers(patterns))
   - `AuthenticationEntryPoint` `AccessDeniedHandler`

---

## TODO:
1. Redis + blacklist
2. 로그아웃
3. Refresh Token

---

## 🖥️ Tip
- `SecurityFilterChain` 기본 설정 @Override 가능(@Bean: 수동, @Component(@Service 포함): 자동)
  - AuthenticationManager, PasswordEncoder, UserDetailsService 필수
- `@Bean` AuthenticationManager 은 하나만 등록 가능(오버로딩 안됨)
- SecurityFilterChain 의 메소드에 따라 적합한 Filter 실행
  - `SecurityFilterChain` HttpSecurityConfiguration, FilterOrderRegistration
  - `AuthenticationFilter` SecurityFilterChain 의 메소드에 따라 적합한 Filter 실행
    - `formLogin` UsernamePasswordAuthenticationFilter, DefaultLoginPageGeneratingFilter, DefaultLogoutPageGeneratingFilter
    - `httpBasic` BasicAuthenticationFilter
    - `oauth2Login` OAuth2LoginAuthenticationFilter
    - `sessionManagement` SessionManagementFilter
  - `AuthenticationManager` Filter 에 적합한 Provider 실행
    - ProviderManager
  - `AuthenticationProvider` 인증 작업 실행
    - `formLogin` DaoAuthenticationProvider
    - `anonymous` AnonymousAuthenticationProvider
    - `rememberMe` RememberMeAuthenticationProvider
- `GenericFilterBean` class 는 addFilter 없이도 자동으로 SecurityFilterChain 에 등록되긴 하지만,  
  어디서 등록했는지 관리 및 순서 보장을 위하여 SecurityFilterChain 에 수동 등록  
  또한, 중복 호출이 가능하기에 extends 한 class `OncePerRequestFilter` 사용(중복 호출 방지)
- JWT / Session 탈취 대응책
  - `Session` Server 에 정보 보관
    - `장점` 탈취 시 제어/초기화 가능
    - `단점` Session 보관소에 문제가 생기면 전체/본인 접근 불가, 서버 비용 부담
  - `JWT` Client 에 정보 보관
    - `장점` 서버 문제로 Token 사용이 불가할 일은 없음(서버와 별개),  
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            역추적 가능(Refresh Token 을 같이 발급하고, 해당 Token 은 서버에 저장해 재시도 시 역으로 강제 로그아웃 + 블랙리스트 등록)
    - `단점` Client 에서 관리하기에 조작 여지가 있고 디코딩 가능하여 민감한 정보는 불가, Access Token 은 탈취 시 발급~만료 시점까지 대응 불가능

---

### ⚙️ 개발 환경
- **IDE** : IntelliJ(Community - 2023.1)
- **Framework** : SpringBoot(3.x) -> v3.4.1
- **Language** : Java 17 -> Java 19(corretto-19)
- **DB** : H2 Database
- **ORM** : Jpa

### 📂 디렉토리 구조(폴더)
```bash
📦main
 ┣ 📂java
 ┃ ┗ 📂com
 ┃ ┃ ┗ 📂example
 ┃ ┃ ┃ ┗ 📂security_test
 ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┣ 📂annotation
 ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┣ 📂enumeration
 ┃ ┃ ┃ ┃ ┃ ┣ 📂exception
 ┃ ┃ ┃ ┃ ┃ ┣ 📂filter
 ┃ ┃ ┃ ┃ ┃ ┣ 📂properties
 ┃ ┃ ┃ ┃ ┃ ┗ 📂security
 ┃ ┃ ┃ ┃ ┣ 📂enumeration
 ┃ ┃ ┃ ┃ ┣ 📂in
 ┃ ┃ ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┣ 📂service
 ┃ ┃ ┃ ┃ ┃ ┗ 📂serviceImpl
 ┃ ┃ ┃ ┃ ┣ 📂out
 ┃ ┃ ┃ ┃ ┃ ┣ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┗ 📂repository
 ┃ ┃ ┃ ┃ ┣ 📂util
 ┃ ┃ ┃ ┃ ┗ 📜SecurityTestApplication.java
 ┗ 📂resources
```

### 📂 디렉토리 구조(파일)
```bash
📦main
 ┣ 📂java
 ┃ ┗ 📂com
 ┃ ┃ ┗ 📂example
 ┃ ┃ ┃ ┗ 📂security_test
 ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┣ 📂annotation
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜LoginMember.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ApiResponse.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂enumeration
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜AuthConstants.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂exception
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜GlobalAccessDeniedHandler.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜GlobalAuthenticationEntryPoint.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜GlobalExceptionHandler.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂filter
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜JwtAuthenticationFilter.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜LoginFilter.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂properties
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜JwtProperties.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂security
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜SpringSecurityConfig.java
 ┃ ┃ ┃ ┃ ┣ 📂enumeration
 ┃ ┃ ┃ ┃ ┃ ┗ 📜MemberRole.java
 ┃ ┃ ┃ ┃ ┣ 📂in
 ┃ ┃ ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RestMemberController.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜LoginRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜MemberRequest.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜MemberService.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂serviceImpl
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberServiceImpl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserDetailsServiceImpl.java
 ┃ ┃ ┃ ┃ ┣ 📂out
 ┃ ┃ ┃ ┃ ┃ ┣ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜EmbMemberInfo.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜GrantedAuthorityEntity.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Member.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserDetailsEntity.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberAuthorityRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜MemberRepository.java
 ┃ ┃ ┃ ┃ ┣ 📂util
 ┃ ┃ ┃ ┃ ┃ ┣ 📜JwtUtil.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜RequestUriUtil.java
 ┃ ┃ ┃ ┃ ┗ 📜SecurityTestApplication.java
 ┗ 📂resources
 ┃ ┣ 📜application.yml
 ┃ ┗ 📜data.sql
```