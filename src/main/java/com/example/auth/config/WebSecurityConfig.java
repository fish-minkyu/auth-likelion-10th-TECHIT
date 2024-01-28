package com.example.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

// Spring Security는 대부분 설정으로 이뤄진다.

// @Configuration
// : "Bean 객체들의 설정을 담고 있는 클래스다"란 의미
// Bean을 비롯해서 여러 설정하기 위한 Bean 객체
@Configuration
public class WebSecurityConfig {
  // 메서드의 결과를 Bean 객체로 관리해주는 어노테이션
  @Bean
  public SecurityFilterChain securityFilterChain(
    HttpSecurity http
  ) throws Exception {
    // authorizeHttpRequests
    // : 메서드 형태로 설정들을 받음
    http
      .csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(
        // 함수형 프로그래밍으로 어떤 것들을 적용해줄지를 메소드 형식으로 전달
        // "no-auth"로 오는 요청은 모두 허가
        auth -> auth
          // 어떤 경로에 대한 설정인지
          .requestMatchers("/no-auth")
          // 이 경로에 도달할 수 있는 설정
          .permitAll()
          .requestMatchers("/users/my-profile")
          .authenticated()
    )
      // html form 요소를 이용해 로그인을 시키는 설정
      .formLogin(
        formLogin -> formLogin
          // 어떤 경로(URL)로 요청을 보내면 로그인 페이지가 나오는지 결정하는 설정
          .loginPage("/users/login")
          // 아무 설정 없이 로그인에 성공한 뒤, 이동할 URL
          .defaultSuccessUrl("/users/my-profile")
          // 실패 시 이동할 URL
          .failureUrl("/users/login?fail")
          // 모두가 접근할 수 있다.
          .permitAll()
      )
    ;
    // 어떤 경로는 접근해도 되고 어떤 경로는 접근하면 안된다란 설정
    return http.build();  // builder pattern을 사용하고 있다.
    // build 하는 것 자체가 예외를 발생시키기 때문에 throws가 필요하다.
  }
}
