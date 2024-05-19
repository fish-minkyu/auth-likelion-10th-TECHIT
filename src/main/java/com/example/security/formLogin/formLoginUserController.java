package com.example.security.formLogin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/formLogin-users")
public class formLoginUserController {
  private final UserDetailsManager manager;
  private final PasswordEncoder passwordEncoder;
  // interface 기반 DI (Strategy Pattern)
  // private final IUserService userService;

  @GetMapping("/home")
  public String home() {
    //NOTE. Spring에서 실행하고 있는 코드 내부에서,
    // 현재 요청에 대해서 누가 접속해서 요청하고 있는지 파악 가능
    log.info(SecurityContextHolder.getContext().getAuthentication().getName());
    return "index";
  }

  @GetMapping("/login")
  public String loginForm() {
    return "login-form";
  }

  // 로그인 한 후에 내가 누군지 확인하기 위한 페이지
  @GetMapping("/my-profile")
  public String myProfile(
      //NOTE. Authentication authentication
      // : Spring Request Mapping Handler가 지원하는 메서드 중 하나,
      // 메서드에 도달할 때 필요로하는 매개변수 타입을 Spring Boot가 알아서 넣어준다는 뜻
      // => 여기선 누가 접속했는지 알려준다.
      Authentication authentication,
      Model model
  ) {
    model.addAttribute("username", authentication.getName());

    // 사용자 이름 출력
    log.info(authentication.getName());
    // getPrincipal
    // : UserDetailsManager에서 사용하고 있는 User 객체 정보가 Principal에 담겨 있다.
    log.info(((User) authentication.getPrincipal()).getUsername());
    return "my-profile";
  }

  // 회원가입 화면
  @GetMapping("/register")
  public String signUpForm() {
    return "register-form";
  }

  @PostMapping("/register")
  public String signUpRequest(
      @RequestParam("username") String username,
      @RequestParam("password") String password,
      @RequestParam("password-check") String passwordCheck
  ) {
    // password == passwordCheck
    if (!password.equals(passwordCheck)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    // 주어진 정보를 바탕으로 새로운 사용자 생성
    manager.createUser(
        User.withUsername(username)
        .password(passwordEncoder.encode(password))
        .build()
    );

    // 회원가입 성공 후 로그인 페이지로
    return "redirect:/formLogin-users/login";
  }
}
