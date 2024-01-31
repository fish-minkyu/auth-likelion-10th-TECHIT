package com.example.auth.config;

import com.example.auth.filters.AllAuthenticatedFilter;
import com.example.auth.jwt.JwtTokenFilter;
import com.example.auth.jwt.JwtTokenUtils;
import com.example.auth.service.JpaUserDetailsManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

// Spring Security는 대부분 설정으로 이뤄진다.

// @Configuration
// : "Bean 객체들의 설정을 담고 있는 클래스다"란 의미
// Bean을 비롯해서 여러 설정하기 위한 Bean 객체
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
  private final JwtTokenUtils jwtTokenUtils;
  private final UserDetailsManager manager;

  // 메서드의 결과를 Bean 객체로 관리해주는 어노테이션
  @Bean
  // Http 관련 보안 설정하는 객체
  public SecurityFilterChain securityFilterChain(
    HttpSecurity http
  ) throws Exception {
    // JWT 이전
    /*// authorizeHttpRequests
    // : 메서드 형태로 설정들을 받음
    http
      .csrf(AbstractHttpConfigurer::disable)
        // Security 5까지 (구버전)
//        .authorizeHttpRequests()
//          .requestMatchers("")
//          .permitAll()
//          .and()

      // 인증 & 인가 설정
      .authorizeHttpRequests(
        // 함수형 프로그래밍으로 어떤 것들을 적용해줄지를 메소드 형식으로 전달
        // "no-auth"로 오는 요청은 모두 허가
        auth -> auth
          // 어떤 경로에 대한 설정인지
          .requestMatchers(
            "/no-auth",
            "/users/home",
            "/tests",
            "/token/issue",
            "/token/validate" // 인증시켜주는 과정을 아직 안만들어서 전체 허용
          )
          // 이 경로에 도달할 수 있는 설정
          .permitAll()
          .requestMatchers("/users/my-profile")
          .authenticated()
          // 로그인 한 사용자는 해당 URL을 허용하지 않겠다.
          .requestMatchers(
            "/users/login",
            "/users/register"
          )
          .anonymous()
          .anyRequest()
          .authenticated()
          // preHandle 테스트 해볼려고 윗부분 다 주석 처리
          // .anyRequest().permitAll()
    )
      // html form 요소를 이용해 로그인을 시키는 설정 (가장 일반적인 방식)
      .formLogin(
        formLogin -> formLogin
          // 어떤 경로(URL)로 요청을 보내면 로그인 페이지가 나오는지 결정하는 설정
          .loginPage("/users/login")
          // 아무 설정 없이 로그인에 성공한 뒤, 이동할 URL
          .defaultSuccessUrl("/users/my-profile")
          // 실패 시 이동할 URL (사용자에게 실패했음을 알리는 설정)
          .failureUrl("/users/login?fail")
      )
      // 로그아웃 설정
      // : 로그아웃 하는 방법은 로그인 방식 상관없이 동일
      .logout(
        logout -> logout
          // 어떤 경로(URL)로 요청을 보내면 로그아웃이 되는지
          // (사용자의 세션을 삭제할지)
          .logoutUrl("/users/logout")
          // 로그아웃 성공시 이동할 페이지
          .logoutSuccessUrl("/users/login")
      )
      // 특정 필터 앞에 나만의 필터를 넣는다.
//       .addFilterBefore(
//        new AllAuthenticatedFilter(),
//        AuthorizationFilter.class // 해당 필터 앞에다가 넣는다.
//      )
      .addFilterBefore(
        new JwtTokenFilter(jwtTokenUtils),
        AuthorizationFilter.class
      )
      ;*/

    http
      // csrf 보안 해제
      // : form Login에서 가지고 있는 보안 취약점이다.
      .csrf(AbstractHttpConfigurer::disable)
      // authorizeHttpRequest
      // : URL에 따른 요청 인가, 내가 어떤 URL에 접근가능한지 설정하고 싶으면 가정 먼저 설정할 곳
      .authorizeHttpRequests(auth -> auth
        .requestMatchers(
          "/no-auth",
          "/users/home",
          "/tests",
          "/token/issue",
          "/token/validate"
        )
        .permitAll()

        .requestMatchers(HttpMethod.GET, "/articles")
        .permitAll()

        .requestMatchers(HttpMethod.POST, "/articles")
        .authenticated()

        .requestMatchers("/users/my-profile")
        .authenticated()

        .requestMatchers(
          //NOTE JWT를 구현하면 해당 URL은 정상적으로 동작하지 않는다.
          // Why? formLogin을 염두에 두고 만든 URL인데
          // formLogin을 통해서 JWT를 발급받을 수 없다.
          // 또한, 브라우저를 통해 세션과 쿠키로 사용자 증명을 하지 않기 때문이다.
          "/users/login",
          // 회원가입은 유효하다.
          // 회원가입을 진행했을 때, 토큰을 발급할 대상을 늘릴 뿐이다.
          "/users/register"
        )
        .anonymous()

        // ROLE에 따른 접근 설정
        .requestMatchers("/auth/user-role")
        // .hasRole("USER") // ROLE 부분 생략하고 적어주기
        //note 인당 역할 하나 부여 시, 관리자 권한이 사용자 권한의 기능도 사용할 수 있게 수정
         .hasAnyRole("USER", "ADMIN")

        .requestMatchers("/auth/admin-role")
        .hasRole("ADMIN")

        // AUTHORITY에 따른 접근 설정
        .requestMatchers("/auth/read-authority")
        // .hasAuthority("READ_AUTHORITY")
        .hasAnyAuthority("READ_AUTHORITY", "WRITE_AUTHORITY")

        .requestMatchers("/auth/write-authority")
        .hasAuthority("WRITE_AUTHORITY")


        .anyRequest()
        .permitAll()
      )
      // OAuth 설정
      .oauth2Login(oauth2Login -> oauth2Login
        .loginPage("/users/login")
      )


    // JWT를 사용하기 때문에 보안 관련 세션 해제
      .sessionManagement(session -> session
        // 세션을 저장하지 않는다. 즉 세션 기반 인증은 사용하지 않겠다. (stateless)
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      )
      // JWT 필터를 권한 필터 앞에 삽입
      .addFilterBefore(
        new JwtTokenFilter(
          jwtTokenUtils,
          manager
        ),
        AuthorizationFilter.class
      )

      ;
    // 어떤 경로는 접근해도 되고 어떤 경로는 접근하면 안된다란 설정
    return http.build();  // builder pattern을 사용하고 있다.
    // build 하는 것 자체가 예외를 발생시키기 때문에 throws가 필요하다.
  }

  // 비밀번호 암호화 클래스
  // : 비밀번호를 암호화 & 해석하는 Bean 객체
  // userDetailsManager가 passwordEncoder을 사용한다.
  // 테스트 환경(WebSecurityConfig)에서 createUser로 인한 순환참조 발생으로 따로 뺏으므로 주석 처리
/*  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }*/


  // Spring은 interface 기반의 @Autowired가 가능하다.
  // 즉, DI 주입이 가능하다.

  // 사용자 정보 관리 클래스
  // : formLogin 객체가 userDetailsManager 객체를 사용한다.
  // Spring Security의 요소들이 사용자가 제공한 데이터가 어떤 사용자인지 파악하는 interface다.
  // 커스텀화하면 UserDetailsService가 필요한 곳에 Bean이 2개가 들어가기 때문에 오류를 일으킬 수 있으므로 주석 처리
/*  @Bean
  public UserDetailsManager userDetailsManager(
    PasswordEncoder passwordEncoder
  ) {
    // 사용자 1
    UserDetails user1 = User.withUsername("user1")
      .password(passwordEncoder.encode("password1"))
      .build();
    // Spring Security에서 기본으로 제공하는,
    // 메모리 기반 사용자 관리 클래스 + 사용자 1
    return new InMemoryUserDetailsManager(user1);
  }*/
}

// formLogin - 사용 -> userDetailsManager
// formLogin - 사용 -> passwordEncoder
// userDetailsManager - 사용 -> passwordEncoder