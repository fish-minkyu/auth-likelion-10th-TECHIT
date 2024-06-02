package com.example.security.interceptors;

import com.example.security.interceptors.TestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/tests")
public class TestController {
  // 요청을 보냈을 때 요청 header와 body가 뭐뭐 있는지 Filter와 Interceptor로 확인해보자
  @GetMapping
  public String test() {
    return "done";
  }

  @PostMapping
  public String testBody(
    @RequestBody TestDto dto
    ) {
    log.info(dto.toString());
    return "done";
  }
}
