package com.example.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {

   // 로그인 화면 (로그인 하기 전에만 볼 수 있음)
   @GetMapping("/login")
   public String loginForm() {
     return "login-form";
   }

   // 로그인 한 후, 내가 누군지 확인하는 페이지 (로그인 하고 난 다음 볼 수 있음)
   @GetMapping("/my-profile")
  public String myProfile() {
     return "my-profile";
   }
}
