package com.oscar.conduit.article.dto.request;

import org.springframework.data.jpa.domain.Specification;

import com.oscar.conduit.article.Article;
import com.oscar.conduit.article.Favorite;
import com.oscar.conduit.article.Tag;
import com.oscar.conduit.user.User;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

public class ArticleSpecification {

  public static Specification<Article> hasTag(String tag) {
    return (root, query, cb) -> {
      if (tag == null)
        return null;
      Join<Article, Tag> tags = root.join("tagList", JoinType.INNER);
      return cb.equal(tags.get("name"), tag);
    };
  }

  public static Specification<Article> hasAuthor(String author) {
    return (root, query, cb) -> {
      if (author == null)
        return null;
      Join<Article, User> authorJoin = root.join("author", JoinType.INNER);
      return cb.equal(authorJoin.get("username"), author);
    };
  }

  public static Specification<Article> favoritedBy(String username) {
    return (root, query, cb) -> {
      if (username == null)
        return null;
      Join<Article, Favorite> favorites = root.join("favorites", JoinType.INNER);
      Join<Favorite, User> user = favorites.join("user", JoinType.INNER);
      return cb.equal(user.get("username"), username);
    };
  }
}
