# Auth

- 2024.01.25 ~ 01.26`10주차`
- 01.25 UserDetailsManager, Authentication, SecurityContextHolder, Strategy Pattern
- 01.26 CustomUserDetails, CustomUserDetailsService, Interceptor, Filter - UserDetailsManager, UserDetails을 커스텀화하여 DB에 사용자 정보 저장
- 01.29 JWT
- 01.30 OAuth

`1월.25일`은 

Spring Security를 통해 로그인을 학습하는 프로젝트다.

`쿠키와 세션`

`UserDetailsManager`  
UserDetailsManager를 새로 만들 수 있다.  
개발자가 커스텀한 UserDetailsManager를 사용한다면   
사용자 계정 정보를 어떻게 관리 및 활용할 것인지 마음대로 편집할 수 있다.

다음엔 UserDetailsManager을 새로 만들어서 DB에 저장하고 사용자 정보를 관리를 할 것이다.

이것이 바로 인터페이스를 기반으로 코딩을 했을 때, 가져올 수 있는 장점이다.  
뒤쪽에 있는 구현체가 어떻든 간에 개발자는 이 기능을 믿고 사용할 수 있다.  
그리고 실제로 어떻게 동작할지는 그 인터페이스를 구현한 개발자가 해줄 수 있다.

`SecurityContextHolder.getContext().getAuthentication().getName()`

`Authentication`

`Strategy Pattern`
interface를 기반 커스터마이징 (Strategy Pattern),  
일반적으로 인터페이스를 쓰고 인터페이스 구현체를 만들어서 사용하는 것이 "템플릿"으로 활용된다.  
인터페이스가 구현이 어떻게 되어있는지 보단, 이런 구현체를 마련해주었다는거에 좀 더 집중을 하는 것이다.  
인터페이스가 정의가 만족되고 있는 클래스 하나를 만들어서 사용자 정보를 관리하고  
그 사용자 정보를 바탕으로 필요한 기능(로그인, 로그아웃 등등)들을 Framework가 알아서 하는 것이다.    
=> 인터페이스를 쓰면 뭐가 좋은지 고민해보기(인터페이스와 구현체를 왜 나누었을까?)  

`1월 26일`

## 스택

- Spring Boot 3.2.2
- Spring Web
- Spring Security
- Lombok
- Thymeleaf
- Jjwt

## Key Point


## 복습
2024.01.27 로그인 복습  
2024.01.28 로그아웃, 회원가입 복습

