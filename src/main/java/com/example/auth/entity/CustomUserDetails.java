package com.example.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;
import java.util.Set;

// UserEntity를 바탕으로 Spring Security 내부에서
// 사용자 정보를 주고받기 위한 객체임을 나타내는 interface UserDetails의 커스텀 구현체
// : UserEntity에 있는 정보들을 나중에 주고 받을 일이 있을 때
// 로그인 한 사용자를 바탕으로 바로 바로 받아올 수 있게 하기 위해 직접 만들었다.
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetails implements UserDetails { // UserDetails가 사용자의 정보를 주고 받기 위한 Dto라고도 볼 수 있다.
  @Getter
  private Long id;
  private String username;
  private String password;
  @Getter
  private String email;
  @Getter
  private String phone;

  @Override
  // getAuthorities는 규약상으로 null을 반환하면 안된다.
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Set.of(); // 빈 set을 반환
  }

  // Getter
  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.username;
  }

  // static factroy method
  public static CustomUserDetails fromEntity(UserEntity userEntity) {
    throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
  }









  // 지금은 다루지 않음
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
