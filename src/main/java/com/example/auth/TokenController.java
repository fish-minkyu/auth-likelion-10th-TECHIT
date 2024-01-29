package com.example.auth;

import com.example.auth.jwt.JwtRequestDto;
import com.example.auth.jwt.JwtResponseDto;
import com.example.auth.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController // 이번 수업엔 서비스를 분리하지 않을 것이다.
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController {

  // 1. JWT를 발급하기 위한 Bean
  private final JwtTokenUtils jwtTokenUtils;
  // 2. 사용자 정보를 회수하기 위한 Bean (나중엔 UserDetailsService를 사용하는 걸 추천)
  private final UserDetailsManager manager; // UserDetailsService도 가능하고 직접 DB에 접속해도 됨
  // 3. 사용자가 제공한 아이디 비밀번호를 비교하기 위한 클래스
  private final PasswordEncoder passwordEncoder;

  // JWT 발급 메서드
  // POST /token/issue
  @PostMapping("/issue")
  public JwtResponseDto issueJwt(
    @RequestBody JwtRequestDto dto
  ) {
    // 1. 사용자가 제공한 username, password가 저장된 사용자인지 판단
    if (!manager.userExists(dto.getUsername()))
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

    UserDetails userDetails
      = manager.loadUserByUsername(dto.getUsername());

    // 2. 비밀번호 대조
    // => 날 것의 비밀번호와 암호화된 비밀번호를 비교한다.
    if (!passwordEncoder
      .matches(dto.getPassword(), userDetails.getPassword()))
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

    // 3. JWT 발급
    String jwt = jwtTokenUtils.generateToken(userDetails);
    JwtResponseDto response = new JwtResponseDto();
    response.setToken(jwt);
    return response;
  }
}
