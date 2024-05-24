# Spring Security

- 2024.01.25 ~ 01.31 `10주차`
- 01.25 Form Login
- 01.26: Form Login + JWT

## 스팩

- Spring Boot 3.2.2
- Spring Web
- Spring Security
- Lombok
- Thymeleaf
- Thymeleaf - springsecurity6
- jjwt
- OAuth2-client


## 수업 파일

<details>
<summary><strong>01/25 Form Login</strong></summary>

- `WebSecurityConfig`: formLogin 설정
- `RootController`: Spring Security 기본 로그인 화면(JWT를 추가하면 나오지 않는다.)
- `UserController`: Form Login 컨트롤러
- `login-form.html`: 로그인 페이지
- `register-form.html`: 회원가입 페이지
- `my-profile.html`: 로그인 후 회원 페이지
- `index.html`: 로그인이 안되어 있다면 로그인 페이지, 로그인이 되었다면 회원 페이지
- `IUserService`: 인터페이스
- `UserServiceImpl`: 인터페이스 구현 클래스

</details>

<details>
<summary><strong>01/26 Form Login + JWT</strong></summary>

- `WebSecurityConfig`: "/tests" 경로 추가를 위해 설정, AllAuthenticatedFilter 등록
- `AuthenticationFacade`: Facade Pattern을 이용한 클래스 파일
- `UserRepository`: DB에 저장하기 위해
- `UserEntity`: DB에 저장하기 위해
- `JPAUserDetailsManager`: 사용자의 정보를 커스텀하기 위해
- `CustomUserDetails`: 사용자의 정보를 커스텀하고 주고 받기 위해
- `TestController`: Interceptor 적용 컨트롤러
- `LoggingInterceptor`: Interceptor 로직 설정
- `InterceptorConfig`: Interceptor 설정
- `TestDto`: Body 데이터를 읽기 위해 만들었다.
- `LogFilter`: Filter를 이용해 Log 찍어보기
- `AllAuthenticatedFilter`: Custom Filter 만들어보기
</details>


