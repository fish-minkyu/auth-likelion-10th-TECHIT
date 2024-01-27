package com.example.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {
  @GetMapping
  public String root() {
    return "hello"; // "hello"란 데이터가 나오지 않는다.
    // because, Spring Security를 추가해주면 접근할 수 있는 URL이 사라지기 때문
  }
}
