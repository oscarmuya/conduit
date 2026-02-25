package com.oscar.conduit.article;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oscar.conduit.article.dto.request.ArticleFilter;
import com.oscar.conduit.article.dto.request.CreateArticleRequest;
import com.oscar.conduit.article.dto.request.UpdateArticleRequest;
import com.oscar.conduit.article.dto.response.ArticleResponse;
import com.oscar.conduit.article.dto.response.ArticlesResponse;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/articles")
public class ArticleController {

  private final ArticleService articleService;

  @PostMapping
  public ResponseEntity<ArticleResponse> handleCreateArticle(Authentication authentication,
      @Valid @RequestBody CreateArticleRequest request) {
    Long userId = Long.parseLong(authentication.getName());
    return ResponseEntity.status(HttpStatus.CREATED).body(articleService.createArticle(request, userId));
  }

  @GetMapping
  public ResponseEntity<ArticlesResponse> getArticles(
      @RequestParam(required = false) String tag,
      @RequestParam(required = false) String author,
      @RequestParam(required = false) String favorited,
      @RequestParam(defaultValue = "20") int limit,
      @RequestParam(defaultValue = "0") int offset,
      @Nullable Authentication authentication) {

    String userId = Optional.ofNullable(authentication)
        .map(Authentication::getName)
        .orElse(null);

    ArticleFilter filter = new ArticleFilter(tag, author, favorited, limit, offset);
    return ResponseEntity.ok(articleService.getArticles(filter, userId));
  }

  @PutMapping("/{slug}")
  public ResponseEntity<ArticleResponse> handleUpdateArticle(Authentication authentication,
      @PathVariable String slug,
      @Valid @RequestBody UpdateArticleRequest request) {
    Long userId = Long.parseLong(authentication.getName());
    return ResponseEntity.status(HttpStatus.OK).body(articleService.updateArticle(slug, request, userId));
  }

  @GetMapping("/{slug}")
  public ResponseEntity<ArticleResponse> handleGetArticle(@Nullable Authentication authentication,
      @PathVariable String slug) {

    String currentUser = Optional.ofNullable(authentication)
        .map(Authentication::getName)
        .orElse(null);

    return ResponseEntity.status(HttpStatus.CREATED).body(articleService.getArticleBySlug(slug, currentUser));
  }

  @DeleteMapping("/{slug}")
  public ResponseEntity<Void> handleDeleteArticle(Authentication authentication,
      @PathVariable String slug) {
    Long userId = Long.parseLong(authentication.getName());
    articleService.deleteArticle(slug, userId);
    return ResponseEntity.noContent().build();
  }

}
