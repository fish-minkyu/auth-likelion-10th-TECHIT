package com.example.auth.jwt;

import lombok.Data;

// 단순 Dto여서 Data를 넣어줬다.
@Data
public class JwtResponseDto {
  private String token;
}
