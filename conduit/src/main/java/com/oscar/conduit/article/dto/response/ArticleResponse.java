package com.oscar.conduit.article.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.oscar.conduit.article.Article;
import com.oscar.conduit.article.Tag;
import com.oscar.conduit.dto.response.ProfileResponse;
import com.oscar.conduit.user.User;

public record ArticleResponse(ArticleData article) {
  public record ArticleData(
      String slug,
      String title,
      String description,
      String body,
      List<String> tagList,

      LocalDateTime createdAt,
      LocalDateTime updatedAt,

      boolean favorited,
      int favoritesCount,
      ProfileResponse author) {
  }

  public static ArticleResponse from(Article article, User author, boolean following, boolean favorited,
      int favoritesCount) {

    ProfileResponse profile = ProfileResponse.from(author, following);
    ArticleData articleData = new ArticleData(article.getSlug(), article.getTitle(), article.getDescription(),
        article.getBody(),
        article.getTagList().stream().map(Tag::getName).toList(), article.getCreatedAt(), article.getUpdatedAt(),
        favorited, favoritesCount, profile);

    return new ArticleResponse(articleData);
  }
}
