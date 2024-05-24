package com.example.security.service;

import com.example.security.entity.CustomUserDetails;
import com.example.security.entity.UserEntity;
import com.example.security.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

// JPAUserDetailsManager(= 커스텀 UserDetailsManager)
// => JPA에 등록되어 있는 사용자를 가져올 수 있게끔 UserDetailsService를 만든 것이다.
@Slf4j
// @RequiredArgsConstructor // 테스트 목적이어서 지움
@Service
public class JpaUserDetailsManager implements UserDetailsManager {
  private final UserRepository userRepository;

  public JpaUserDetailsManager(
    UserRepository userRepository,
    // createUser 메소드 내부에서 암호화를 진행하지 않고 외부에서 암호화된 비밀번호를 전달해줬다고 가정
    // So, 의존성으로 추가될 필요가 없다.
    // Because, createUser 메소드 내부에서 암호화 사용 시, passwordEncoder가 필드로서 의존성 주입을 해줘야 하지만
    // 이렇게 생성자에서만 사용을 한다면 의존성 주입을 해줄 필요가 없다.
    PasswordEncoder passwordEncoder
  ) {
    this.userRepository = userRepository;
    // 오롯이 테스트 목적의 사용자를 추가하는 용도
    // UserDetails 사용
/*    createUser(User.withUsername("user1")
      .password(passwordEncoder.encode("password1"))
      .build()
    );
    createUser(User.withUsername("user2")
      .password(passwordEncoder.encode("password2"))
      .build()
    );
*/

    // CustomUserDetails 사용
    // 사용자
    createUser(CustomUserDetails.builder()
      .username("user")
      .password(passwordEncoder.encode("password"))
      .email("user1@gmail.com")
      .phone("01012345678")
      .authorities("ROLE_USER,READ_AUTHORITY") // 향 후, 테이블을 만들어서 get하기
      .build());

    // 사용자 & 관리자
    createUser(CustomUserDetails.builder()
      .username("admin")
      .password(passwordEncoder.encode("password"))
      .email("user1@gmail.com")
      .phone("01012345678")
      // hasRole() 사용
      // .authorities("ROLE_USER,ROLE_ADMIN")
      //Note. 인당 역할 하나 부여, hasAnyRole() 사용
      // .authorities("ROLE_ADMIN")
      .authorities("ROLE_ADMIN,WRITE_AUTHORITY")
      .build());
  }

  @Override
  // formLogin 등 Spring Security 내부에서
  // 인증을 처리할 때 사용하는 메서드다. (제일 중요하다.)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<UserEntity> optionalUser
      = userRepository.findByUsername(username);

    if (optionalUser.isEmpty())
      // UserDetailsService의 에러 객체, UserDetailsService 인터페이스의 규약에서 명시된 에러이므로 사용.
      throw new UsernameNotFoundException(username);

    UserEntity userEntity = optionalUser.get();

    // CustomUserDetails 반환
    return CustomUserDetails.builder()
      .username(userEntity.getUsername())
      .password(userEntity.getPassword())
      .email(userEntity.getEmail())
      .phone(userEntity.getPhone())
      .authorities(userEntity.getAuthorities())
      .build();

    // UserDeatils 반환
/*    return User.withUsername(username)
      .password(optionalUser.get().getPassword())
      .build();
*/
  }

  @Override
  // 편의를 위해 같은 규약으로 회원가입을 하는 메서드
  public void createUser(UserDetails user) { // UserDetails는 사용자의 정보를 담고 주고 받기 위한 용도의 객체다. 로그인 시, 사용자의 자체를 나타낸다.
    if (this.userExists(user.getUsername()))
      // Security 내부에서 쓰는게 아닌 개발자의 편의를 위해 사용하기 때문에
      // ResponseStatusException()을 쓰는게 맞다.
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

    // CustomUserDetails 사용
    try {
      CustomUserDetails userDetails
        = (CustomUserDetails) user; // user는 UserDetails다.
      // UserDetails와 UserEntity는 의미상, 서로 같아야 하므로 createUser 메소드 내부에서 암호화를 해주는 것이 아닌
      // (그럼 UserDetails와 UserEntity는 비밀번호가 서로 다르므로 서로 같은 객체라고 보기 어렵다.)
      // 외부에서 비밀번호를 암호화해서 넣어주는 것이 맞다.
      UserEntity newUser = UserEntity.builder()
        .username(userDetails.getUsername())
        .password(userDetails.getPassword())
        .email(userDetails.getEmail())
        .phone(userDetails.getPhone())
        .authorities(userDetails.getRawAuthorities())
        .build();
      userRepository.save(newUser);
    } catch (ClassCastException e) {
      log.error("Failed Cast to: {}", CustomUserDetails.class);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

      // UserDetails 사용
/*    UserEntity userEntity = UserEntity.builder()
      .username(user.getUsername())
      .password(user.getPassword()) // user.getPassword()는 암호화된 비밀번호다. (UserController에서 암호화가 되어 넘어왔다.)
      .build();
    userRepository.save(userEntity);
*/
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
