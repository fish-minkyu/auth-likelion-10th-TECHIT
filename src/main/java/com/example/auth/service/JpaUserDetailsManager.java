package com.example.auth.service;

import com.example.auth.entity.UserEntity;
import com.example.auth.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

// 커스텀 UserDetailsManager
// => Jpa에 등록되어 있는 사용자를 가져올 수 있게끔 UserDetailsService를 만든 것이다.
@Slf4j
@RequiredArgsConstructor
@Service
public class JpaUserDetailsManager implements UserDetailsManager {
  private final UserRepository userRepository;

  @Override
  // formLogin 등 Spring Security 내부에서
  // 인증을 처리할 때 사용하는 메서드다. (제일 중요하다.)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<UserEntity> optionalUser
      = userRepository.findByUsername(username);

    if (optionalUser.isEmpty())
      throw new UsernameNotFoundException(username); // UserDetailsService의 에러 객체

    return User.withUsername(username).build();
  }

  @Override
  // 편의를 위해 같은 규약으로 회원가입을 하는 메서드
  public void createUser(UserDetails user) {
    if (this.userExists(user.getUsername()))
      // Security 내부에서 쓰는게 아닌 개발자의 편의를 위해 사용하기 때문에
      // ResponseStatusException()을 쓰는게 맞다.
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    UserEntity userEntity = UserEntity.builder()
      .username(user.getUsername())
      .password(user.getPassword())
      .build();
    userRepository.save(userEntity);
  }

  @Override
  public boolean userExists(String username) {
    return userRepository.existsByUsername(username);
  }


  // 나중에..
  @Override
  public void updateUser(UserDetails user) {
    throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
  }

  @Override
  public void deleteUser(String username) {
    throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
  }

  @Override
  public void changePassword(String oldPassword, String newPassword) {
    throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
  }
}
