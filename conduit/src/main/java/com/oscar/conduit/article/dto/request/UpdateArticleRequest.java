package com.oscar.conduit.article.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record UpdateArticleRequest(@NotNull @Valid Article article) {
  public record Article(
      String title,
      String description,
      String body,
      List<String> tagList) {
  }
}
