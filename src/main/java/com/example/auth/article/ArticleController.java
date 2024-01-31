package com.example.auth.article;

import com.example.auth.article.dto.ArticleDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {
  private final ArticleService service;

  @PostMapping
  public ArticleDto create(
    @RequestBody ArticleDto dto
  ) {
    return service.create(dto);
  }
}
