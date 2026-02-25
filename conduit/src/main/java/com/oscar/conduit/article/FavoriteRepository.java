package com.oscar.conduit.article;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
  boolean existsByUserIdAndArticleId(Long userId, Long articleId);

  int countByArticleId(Long articleId);
}
