package com.example.security.article.dto;

import com.example.security.article.entity.Article;
import lombok.*;

// 어떤 정보까지 필요할까?
// User 객체를 받아오면 너무 많은 정보가 왔다가게 되므로
// 필요한 username만 받아오게 한다.
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDto {
  private Long id;
  @Setter
  private String title;
  @Setter
  private String content;
  @Setter
  private String writer; // writer의 username

  // static factory method
  public static ArticleDto fromEntity(Article entity) {
    return ArticleDto.builder()
      .id(entity.getId())
      .title(entity.getTitle())
      .content(entity.getContent())
      .writer(entity.getWriter().getUsername())
      .build();
  }



  // +) 중첩되어서 Json이 나오게 된다.
//  private ArticleWriterDto writer;
}
