package com.example.security.config;


import com.example.security.filters.AllAuthenticatedFilter;
import com.example.security.jwt.JwtTokenFilter;
import com.example.security.jwt.JwtTokenUtils;
import com.example.security.oauth.OAuth2SuccessHandler;
import com.example.security.oauth.OAuth2UserServiceImpl;
import com.example.security.service.JpaUserDetailsManager;
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
// @Bean을 비롯해서 여러 설정하기 위한 Bean 객체
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
  private final JwtTokenUtils jwtTokenUtils;
  private final UserDetailsManager manager;

  // 메서드의 결과를 Bean 객체로 관리해주는 어노테이션
  @Bean
  // Http 관련 보안 설정하는 객체
  public SecurityFilterChain securityFilterChain(
    // 보안관련 설정들을 모아서 builder 패턴으로 SecurityFilterChain을 설정한다.
    HttpSecurity http
  ) throws Exception {
    // http가 authorizeHttpRequests란 메서드를 가지고 있다.
    // JWT를 하면 세션을 이용할 필요가 없다.
    http
        // csrf 보안 해제
        .csrf(AbstractHttpConfigurer::disable)
        // URL에 따른 요청 인가
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/no-auth",
                "/users/home",
                "/tests/**",
                "/token/issue",
                "/token/validate"
            )
            .permitAll()

            .requestMatchers(HttpMethod.GET, "/articles")
            .permitAll()

            .requestMatchers(HttpMethod.POST, "/articles")
            .authenticated()

            .requestMatchers(
                "/users/my-profile"
            )
            .authenticated()
            .requestMatchers(
                "/users/login",
                "/users/register"
            )
            .anonymous()

            // ROLE에 따른 접근 설정
            .requestMatchers("/auth/user-role")
            //Note. hasAnyRole() 사용
             .hasAnyRole("USER", "ADMIN")

            .requestMatchers("/auth/admin-role")
            //Note. hasRole() 사용
            .hasRole("ADMIN")

            // AUTHORITY에 따른 접근 설정
            .requestMatchers("/auth/read-authority")
            //Note. hasAuthority() 사용
            .hasAuthority("READ_AUTHORITY") // 풀네임 다 적어줘야 한다.

            //Note. hasAnyAuthority() 사용
            .requestMatchers("/auth/write-authority")
            .hasAnyAuthority("READ_AUTHORITY", "WRITE_AUTHORITY")

            .anyRequest()
            .permitAll()
        )
        // JWT를 사용하기 때문에 보안 관련 세션 해제
        .sessionManagement(session -> session
            // 세션을 저장하지 않는다.
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

    return http.build();
  }
}
