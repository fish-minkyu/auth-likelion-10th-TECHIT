package com.example.security.filters;

import com.example.security.entity.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

// Bean으로 등록해주지 않는다.
// : 상황에 따라 Bean으로 등록하면 안해줘야 하는 상황이 있을 수 있다.
// 왜냐하면 해당 객체는 Filter이므로 Spring Boot에서 관리하는 것이 아니므로
// 인증 관련된 필터만큼은 Bean으로 사용하지 않고
// Spring Security에서 수동으로 만들어서 등록하는 것이 관습이다.

// 모든 요청이 인증된 요청으로 취급하는 필터 (실제 서비스에서는 등장해선 안되는 필터)
@Slf4j
public class AllAuthenticatedFilter extends OncePerRequestFilter { // OncePerRequestFilter: 필터를 한번만 실행시키게 해주는 클래스

  @Override
  // Spring에서 사용하기 편하도록 추가적인 기능을 제공해주는 필터
  // 해당 메서드를 구현해줘야 필터를 구현할 수 있다.
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    log.info("try all auth filter");
    // TODO 여기서부터

    // 헤더를 포함하고 있는지 테스트
    // 헤더에 "x-likelion-all-auth: true"가 포함된 요청은 로그인 한 요청이다.
    String header = request.getHeader("x-likelion-all-auth");
    if (header != null) {
      // SecurityContext
      // : 사용자의 인증 정보를 담고 있는 객체

      // 비어있는 SecurityContextHolder을 생성한다.
      SecurityContext context = SecurityContextHolder.createEmptyContext();
      AbstractAuthenticationToken authentication =
        // UsernamePasswordAuthenticationToken(CustomUserDetails, credentials, Authority)
        // : 사용자의 계정과 비밀번호가 일치되었을 때, 만들어지는 Authentication이다.
        new UsernamePasswordAuthenticationToken(
          CustomUserDetails.builder()
            // 사용자의 요청의 x-likelion-all-auth 헤더에 따라 username을 바꿔보자
            .username(header)
            .password("아무거나")
            .phone("010-1234-5678")
            .build(),
          "아무거나", // 비밀번호에 해당
          new ArrayList<>() // 권한을 담아놓는 객체, 지금은 권한이 없으므로 빈 ArrayList 전달
        );
      // 인증정보 맥락에 사용자 정보를 등록한다.
      context.setAuthentication(authentication);
      SecurityContextHolder.setContext(context);
      log.info("set security context with header");
    } else {
      log.info("all auth required header is absent");
    }
    //TODO 여기까지는 어떤 인증방법을 할 것이냐에 따라서 달라질 것이다.
    // 실패하든 말든 필터를 실행을 해주어야 한다.
    // doFilter()를 해줘야 뒤에 있는 필터들이 마저 실행이 되면서
    // 연속적으로 Spring Boot에 도달할 수 있게 된다.

    // 실패하든 말든 필터를 실행을 해줘야 한다.
    filterChain.doFilter(request, response);
  }
}
