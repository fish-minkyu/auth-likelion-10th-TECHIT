package com.example.security.article.dto;

// 만약, 다른 서비스에서 해당 writer의 정보를 커스텀해주고 싶다면
// 새로운 Dto(ArticleWriterDto)를 만들고 기존 Dto(ArticleDto)에 넣어주면 된다.

// 예시, 사용자의 이름과 등급을 표시
public class ArticleWriterDto {
  private String username;
  private String grade;
}
