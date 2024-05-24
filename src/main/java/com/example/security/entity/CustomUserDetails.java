package com.example.security.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

// CustomUserDetails.
// : UserEntity를 바탕으로 Spring Security 내부에서
// 사용자 정보를 주고받기 위한 객체임을 나타내는 interface UserDetails의 커스텀 구현체(UserEntity를 묘사한 객체)

// 만든 이유.
// : UserEntity에 있는 정보들을 나중에 주고 받을 일이 있을 때,
// 로그인 한 사용자를 바탕으로 바로 바로 받아올 수 있게 하기 위해 직접 만들었다.
// 마치 DTO의 역할을 한다.
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
  // 권한 데이터를 담기 위한 속성
  private String authorities;

  // getAuthorities 메소드가 이미 있어서 따로 만듬
  public String getRawAuthorities() {
      return this.authorities;
  }


  @Override
  //Note. 사용자의 권한을 파악하는 메소드
  // 이 메소드가 무엇을 반환하는지에 따라 동작할 수 있는 서비스가 달라진다.
  // GrantedAuthority는 문자열을 반환하는 메서드 하나만 가지고 있다.
  // => CustomUserDetails는 권한을 전달하기 위한 매개체가 된다.
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // Stream
/*    return Arrays.stream(authorities.split(","))
      .sorted()
      .map(SimpleGrantedAuthority::new)
      .collect(Collectors.toList());*/

    List<GrantedAuthority> grantedAuthorities
      = new ArrayList<>();

    String[] rawAuthorities = authorities.split(",");
    for (String rawAuthority : rawAuthorities) {
      grantedAuthorities.add(new SimpleGrantedAuthority(rawAuthority));
    }

    return grantedAuthorities;
    // return List.of(new SimpleGrantedAuthority("ROLE_USER"));
  }
  // +) getAuthorities는 규약상으로 null을 반환하면 안된다.

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







// ----------------------------------------------------------------------

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
