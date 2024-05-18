package com.example.security.jwt;

import lombok.Data;

// 단순 Dto여서 Data를 넣어줬다.
@Data // Getter Setter Value ToString Equals HashCode
public class JwtRequestDto {
  private String username;
  private String password;
}
