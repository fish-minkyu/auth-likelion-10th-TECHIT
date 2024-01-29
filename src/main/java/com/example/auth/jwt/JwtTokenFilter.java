package com.example.auth.jwt;

import com.example.auth.entity.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;


// 인증 관련 객체는 Bean 객체로 등록하지 않는다. (Bean 객체로 등록하면 자동으로 Filter로 등록이 된다.)
// Why? WebSecurityConfig에서 수동으로 등록을 해줘야 한다.
// 근데 Bean으로 등록을 해주면 Spring Container가 한번 더 등록하고 Security에서 등록을 하게되면 2번 등록하게 된다.
@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
  private final JwtTokenUtils jwtTokenUtils;


  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    log.debug("try jwt filter");
    // 1. Authorization 헤더를 회수
    String authHeader
      // = request.getHeader("Authorization");
      // "Authorization"을 자주 써서 상수로 만들어줬다.
    = request.getHeader(HttpHeaders.AUTHORIZATION);
    // 2. Authorization 헤더가 존재하는지 + Bearer로 시작하는지
    if (authHeader != null && authHeader.startsWith("Bearer")) {
      String token = authHeader.split(" ")[1];
      // 3. Token이 유효한 토큰인지 검사
      if (jwtTokenUtils.validate(token)) {
        // 4. 유효하다면 해당 토큰을 바탕으로 사용자 정보를 SecurityContext에 등록
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        // 사용자 정보 회수
        String username = jwtTokenUtils
          .parseClaims(token)
          .getSubject();
        // 인증 정보 생성
        AbstractAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(
            CustomUserDetails.builder()
              .username(username)
              .build(),
            token, new ArrayList<>()
          );
        // 인증 정보 등록
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        log.info("set security context with jwt");
      }
      else {
        // 사용자가 잘못된 jwt를 요청할 수 있으므로 기록해둔다.
        log.warn("jwt validation failed");
      }
    }

    // 5. 다음 필터 호출
    // doFilter를 호출하지 않으면 Controller까지 요청이 도달하지 못한다.
    filterChain.doFilter(request, response);
  }
}
