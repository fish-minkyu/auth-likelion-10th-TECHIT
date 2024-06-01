package com.example.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// 지금은 테스트 목적 의존성 때문에 문제가 발생하지만
// 순환 참조는 허용되지 않으니 지금처럼 바깥으로 빼주면 된다.
// WebSecurityConfig를 생성할려면 JpaUserDetailsManager가 필요.
// JpaUserDetailsManager를 생성할려면 WebSecurityConfig 안에 있는 PasswordEncoder가 필요.
// 그래서 순환참조가 발생.

@Configuration
public class PasswordEncoderConfig {
  @Bean
  // 비밀번호 암호화 클래스
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
