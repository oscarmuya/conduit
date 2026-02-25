package com.oscar.conduit.article;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oscar.conduit.article.dto.response.TagResponse;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/tags")
public class TagController {
  private final ArticleService articleService;

  @GetMapping
  public ResponseEntity<TagResponse> handleGetTags() {
    return ResponseEntity.status(HttpStatus.OK).body(articleService.getTags());
  }
}
