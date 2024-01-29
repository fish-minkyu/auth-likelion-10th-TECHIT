package com.example.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/tests")
public class TestController {
  // 요청을 보냈을 때 header와 body가 뭐뭐 있는지 Filter와 Interceptor로 확인해보자
  @GetMapping
  public String test() {
    return "done";
  }
}
