package com.example.auth.oauth;

import com.example.auth.entity.CustomUserDetails;
import com.example.auth.jwt.JwtTokenUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

// OAuth2UserServiceImpl이 성공적으로 OAuth2 과정을 마무리 했을 때,
// 넘겨받은 사용자 정보를 바탕으로 JWT를 생성,
// 클라이언트한테 JWT를 전달
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler
  // 인증에 성공했을 때, 특정 URL로 리다이렉트 하고 싶은 경우 활용 가능한 SuccessHandler
  extends SimpleUrlAuthenticationSuccessHandler {
  // JWT 발급을 위해 JwtTokenUtils 필요
  private final JwtTokenUtils tokenUtils;
  // 사용자 정보 등록을 위해 UserDetailsManager
  private final UserDetailsManager userDetailsManager;

  @Override
  // 인증 성공 시, onAuthenticationSuccess 실행
  public void onAuthenticationSuccess(
    HttpServletRequest request,
    HttpServletResponse response,
    Authentication authentication // SecurityContext와 동일, 차이점은 DefaultOAuth2User가 담겨져있다.
  ) throws IOException, ServletException {
    // OAuth2UserServiceImpl의 반환값이 할당된다.
    OAuth2User oAuth2User
      = (OAuth2User) authentication.getPrincipal();

    // 넘겨받은 정보를 바탕으로 사용자 정보를 준비
    String email = oAuth2User.getAttribute("email");
    String provider = oAuth2User.getAttribute("provider");
    String username
      = String.format("{%s}%s", provider, email);
    String providerId = oAuth2User.getAttribute("id").toString();
    // 처음으로 이 소셜 로그인으로 로그인을 시도했다.
    if (!userDetailsManager.userExists(username)) {
      // 새 계정을 만들어야 한다.
      userDetailsManager.createUser(CustomUserDetails.builder()
        .username(username)
        .email(email)
        .password(providerId) // 이 용도는 아니지만 일단 providerId를 집어넣어보자.
        .authorities("ROLE_USER")
        .build());
    }

    // 데이터베이스에서 사용자 계정 회수
    UserDetails details
      = userDetailsManager.loadUserByUsername(username);

    // JWT 생성
    String jwt = tokenUtils.generateToken(details);
    // 어디로 리다이렉트할지 지정(클라이언트에게 데이터를 전달하기 위해 리다이렉트 사용)
    String targetUrl = String.format(
      "http://localhost:8080/token/validate?token=%s", jwt
    );
    getRedirectStrategy().sendRedirect(request, response, targetUrl);
  }
}
