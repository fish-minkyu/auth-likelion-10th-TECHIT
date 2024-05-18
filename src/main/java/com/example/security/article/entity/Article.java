package com.example.security.article.entity;

import com.example.security.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Article {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter
  private String title;
  @Setter
  private String content;

  @Setter
  @ManyToOne
  private UserEntity writer; // 사용자 정보 포함
}
