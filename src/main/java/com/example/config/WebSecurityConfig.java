package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.web.bind.annotation.RequestMethod;

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
    http.authorizeHttpRequests(
      // 함수형 프로그래밍으로 어떤 것들을 적용해줄지를 메소드 형식으로 전달
      // "no-auth"로 오는 요청은 모두 허가
      auth -> auth.requestMatchers("/no-auth").permitAll() // 해당 경로로 오는 요청들은 모두 허가한다.
    );
    // 어떤 경로는 접근해도 되고 어떤 경로는 접근하면 안된다란 설정
    return http.build();  // builder pattern을 사용하고 있다.
    // build 하는 것 자체가 예외를 발생시키기 때문에 throws가 필요하다.
  }
}
