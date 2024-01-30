package com.example.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@Table(name = "user_table")
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String username;
  private String password;

  private String email;
  private String phone;

  // 테스트를 위해서 문자열 하나에 ','로 구분해 권한을 묘사
  // (정석은 사용자 : 역할 = M : N)
  // ROLE_USER,ROLE_ADMIN,READ_AUTHORITY,WRITE_AUTHORITY
  private String authorities;
}
