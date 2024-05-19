package com.example.security.config;


import com.example.security.jwt.JwtTokenUtils;
import com.example.security.oauth.OAuth2SuccessHandler;
import com.example.security.oauth.OAuth2UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

// Spring Security는 대부분 설정으로 이뤄진다.

// @Configuration
// : "Bean 객체들의 설정을 담고 있는 클래스다"란 의미
// @Bean을 비롯해서 여러 설정하기 위한 Bean 객체
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
  private final JwtTokenUtils jwtTokenUtils;
  private final UserDetailsManager manager;
  private final OAuth2UserServiceImpl oAuth2UserService;
  private final OAuth2SuccessHandler oAuth2SuccessHandler;

  // 메서드의 결과를 Bean 객체로 관리해주는 어노테이션
  @Bean
  // Http 관련 보안 설정하는 객체
  public SecurityFilterChain securityFilterChain(
    // 보안관련 설정들을 모아서 builder 패턴으로 SecurityFilterChain을 설정한다.
    HttpSecurity http
  ) throws Exception {
    // http가 authorizeHttpRequests란 메서드를 가지고 있다.
    http
        .csrf(AbstractHttpConfigurer::disable)
        // URL에 대한 설정
        .authorizeHttpRequests(
      // Spring Boot가 3버전으로 올라감으로써, 함수형 프로그래밍 방식인 메서드 형식으로 인자를 전달해준다.
      // 설정을 진행되는 인자를 메서드 형태로 받는 것이다.
      auth -> auth
          // 어떤 경로에 대한 설정인지
          .requestMatchers("/no-auth",
              "/formLogin-users/home")
          // 이 경로에 도달할 수 있는 사람에 대한 설정
          .permitAll() // 모두
          .requestMatchers("/formLogin-users/my-profile")
          .authenticated() // 인증
          .requestMatchers(
              "/formLogin-users/login",
              "/formLogin-users/register"
          )
          .anonymous()
          .anyRequest()
          .authenticated()
    )
    // html form 요소를 이용해 로그인을 시키는 설정 (가장 일반적인 방법)
    .formLogin(
      formLogin -> formLogin
          // 어떤 경로(URL)로 요청을 보내면
          // 로그인 페이지가 나오는지
          .loginPage("/formLogin-users/login")
          // 아무 설정없이 로그인에 성공한 뒤, 이동할 URL
          .defaultSuccessUrl("/formLogin-users/my-profile")
          // 실패시 이동할 URL
          .failureUrl("/formLogin-users/login?fail")
    )
        // 로그아웃 설정
        .logout(
            logout -> logout
            // 어떤 경로(URL)로 요청을 보내면 로그아웃이 되는지(사용자의 세션을 삭제할지)
            .logoutUrl("/formLogin-users/logout")
            // 로그아웃 성공시 이동할 페이지
            .logoutSuccessUrl("/formLogin-users/login"))
    ;

    return http.build();
  }

  // 비밀번호 암호화 클래스
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // formLogin 방식은 UserDetailsManager를 사용하고 있다.
  // UserDetailsManager: 사용자 정보 관리 클래스
  @Bean
  public UserDetailsManager userDetailsManager() {
    // 사용자 1
    UserDetails user1 = User.withUsername("user1")
        .password(passwordEncoder().encode("password1"))
        .build();

    // Spring Security에서 기본으로 제공하는,
    // 메모리 기반 사용자 관리 클래스 + 사용자 1
    return new InMemoryUserDetailsManager(user1);
  }
}

// formLogin은 PasswordEncoder, UserDetailsManager 사용
// UserDetailsManager도 PasswordEncoder를 사용