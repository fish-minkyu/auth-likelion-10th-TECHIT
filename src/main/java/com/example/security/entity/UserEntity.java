package com.example.security.entity;

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

  // (정석은 사용자 Entity : 역할 Entity= M : N이며 List 또는 Set으로 설정한다.)
  // 테스트를 위해서 문자열 하나에 ','로 구분해 권한을 묘사
  // Ex. ROLE_USER,ROLE_ADMIN,READ_AUTHORITY,WRITE_AUTHORITY
  private String authorities;
}
