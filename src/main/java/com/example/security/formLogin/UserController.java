package com.example.security.formLogin;

import com.example.security.AuthenticationFacade;
import com.example.security.entity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
@RequestMapping("/users")
public class UserController {
  private final UserDetailsManager manager; // JpaUserDetailsManager (다형성으로 주입 가능)
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationFacade authFacade;
  // interface 기반 DI (Strategy Pattern)
  // private final IUserService userService;

  @GetMapping("/home")
  public String home() {
    //NOTE. Spring에서 실행하고 있는 코드 내부에서,
    // 현재 요청에 대해서 누가 접속해서 요청하고 있는지 파악 가능
    log.info(SecurityContextHolder.getContext().getAuthentication().getName());
    log.info(authFacade.getAuth().getName());
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
      // : 현재 접근하는 주체의 정보와 권한을 담는 인터페이스다.
      Authentication authentication,
      Model model
  ) {
    model.addAttribute("username", authentication.getName());

    // 사용자 이름 출력
    log.info(authentication.getName());
    // getPrincipal (반환타입은 Principal, Principal은 사용자 정보가 담겨져 있다)
    // : UserDetailsManager에서 사용하고 있는 User 객체 정보가 Principal에 담겨 있다.
//    log.info(((User) authentication.getPrincipal()).getUsername());

    //Note. CustomUserDetails를 사용하면 커스텀한 사용자의 정보를 가져올 수 있음을 기대할 수 있다.
    log.info(((CustomUserDetails) authentication.getPrincipal()).getPassword());
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

    // 회원가입 시, 사용자의 정보를 직접 다루고 싶다면 CustomUserDetails 사용
    // CustomUserDetails 사용
    manager.createUser(CustomUserDetails.builder()
            .username(username)
            .password(passwordEncoder.encode(password))
            .build());

    // User 사용, 주어진 정보를 바탕으로 새로운 사용자 생성
/*    manager.createUser(
        User.withUsername(username)
        .password(passwordEncoder.encode(password))
        .build()
    );*/

    // 회원가입 성공 후 로그인 페이지로
    return "redirect:/formLogin-users/login";
  }
}
