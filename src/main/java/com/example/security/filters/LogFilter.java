package com.example.security.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component // Filter는 @Component로 등록하면 Spring Boot가 자동을 Filter로 등록해준다.
public class LogFilter implements Filter {

  @Override
  public void doFilter(
    ServletRequest request,
    ServletResponse response,
    FilterChain chain // 여러 개의 Filter들을 묶어 놓은 것
  ) throws IOException, ServletException {
    HttpServletRequest httpServletRequest = (HttpServletRequest) request;

    log.info("start request: {} {}",
        httpServletRequest.getMethod(),
        httpServletRequest.getRequestURI()
      );
    // doFilter를 호출하면 다음 필터로 요청과 응답 객체를 넘겨주겠다란 의미가 된다.
    // doFilter를 호출하지 않을 경우, 다음 필터가 실행되지 않으며
    // -> 요청이 끝까지 전달되지 않는다.

    // --- 이 위는 요청 처리 전
    chain.doFilter(request, response);
    // --- 이 아래는 요청 처리 후

    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
    log.info("send response {}", httpServletResponse.getStatus());
  }
}
