package com.oscar.conduit.article.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateArticleRequest(@NotNull @Valid Article article) {
  public record Article(
      @NotBlank(message = "Title is needed") String title,
      @NotBlank(message = "Description is needed") String description,
      @NotBlank(message = "Body is needed") String body,
      List<String> tagList) {
  }
}
