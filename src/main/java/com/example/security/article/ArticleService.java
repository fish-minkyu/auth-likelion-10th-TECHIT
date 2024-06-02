package com.example.security.article;

import com.example.security.article.dto.ArticleDto;
import com.example.security.article.entity.Article;
import com.example.security.article.repo.ArticleRepository;
import com.example.security.entity.UserEntity;
import com.example.security.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {
  private final ArticleRepository articleRepository;
  private final UserRepository userRepository;

  public ArticleDto create(ArticleDto dto) {

    UserEntity writer = getUserEntity();

    // UserEnity 설정
    Article newAritcle = Article.builder()
      .title(dto.getTitle())
      .content(dto.getContent())
      .writer(writer)
      .build();

    // 저장
    newAritcle = articleRepository.save(newAritcle);
    return ArticleDto.fromEntity(newAritcle);
  }

  // 사용자 정보 가져오기 - SecurityContextHolder에서 사용자 가져오기
  // : SecurityContextHolder 를 이용하면 어디서든지 User의 정보를 받아올 수 있다.
  private UserEntity getUserEntity() {
    UserDetails userDetails =
      (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    // Authentication -> UserDetails로 형변환을 해줬기에
    // getUsername이 반드시 있을 것이라 기대할 수 있다.
    return userRepository.findByUsername(userDetails.getUsername())
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }
}
