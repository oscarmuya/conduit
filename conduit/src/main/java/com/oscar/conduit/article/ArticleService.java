package com.oscar.conduit.article;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.oscar.conduit.article.dto.request.ArticleFilter;
import com.oscar.conduit.article.dto.request.ArticleSpecification;
import com.oscar.conduit.article.dto.request.CreateArticleRequest;
import com.oscar.conduit.article.dto.request.UpdateArticleRequest;
import com.oscar.conduit.article.dto.response.ArticleResponse;
import com.oscar.conduit.article.dto.response.ArticlesResponse;
import com.oscar.conduit.article.dto.response.TagResponse;
import com.oscar.conduit.exception.ArticleNotFoundException;
import com.oscar.conduit.exception.UnauthorizedException;
import com.oscar.conduit.exception.UserNotFoundException;
import com.oscar.conduit.follow.FollowRepository;
import com.oscar.conduit.user.User;
import com.oscar.conduit.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ArticleService {
  private final ArticleRepository articleRepository;
  private final TagRepository tagRepository;
  private final UserRepository userRepository;
  private final FollowRepository followRepository;
  private final FavoriteRepository favoriteRepository;

  public ArticleResponse createArticle(CreateArticleRequest request, Long userId) {
    CreateArticleRequest.Article article = request.article();
    User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

    List<String> tagNames = article.tagList() != null ? article.tagList() : List.of();
    List<Tag> tagList = tagNames.stream()
        .map(name -> tagRepository.findByName(name)
            .orElseGet(() -> tagRepository.save(Tag.builder().name(name).build())))
        .toList();

    Article newArticle = Article.builder()
        .tagList(tagList)
        .title(article.title())
        .body(article.body())
        .description(article.description())
        .author(user)
        .slug(toSlug(article.title()))
        .build();

    Article savedArticle = articleRepository.save(newArticle);
    return ArticleResponse.from(savedArticle, user, false, false, 0);
  }

  public void deleteArticle(String slug, Long userId) {
    Article article = articleRepository.findBySlug(slug)
        .orElseThrow(ArticleNotFoundException::new);

    if (!article.getAuthor().getId().equals(userId)) {
      throw new UnauthorizedException();
    }

    articleRepository.delete(article);
  }

  public ArticleResponse updateArticle(String slug, UpdateArticleRequest request, Long userId) {
    Article article = articleRepository.findBySlug(slug)
        .orElseThrow(ArticleNotFoundException::new);

    if (!article.getAuthor().getId().equals(userId)) {
      throw new UnauthorizedException();
    }

    UpdateArticleRequest.Article data = request.article();

    if (data.title() != null) {
      article.setTitle(data.title());
      article.setSlug(toSlug(data.title()));
    }
    if (data.description() != null) {
      article.setDescription(data.description());
    }
    if (data.body() != null) {
      article.setBody(data.body());
    }
    if (data.tagList() != null) {
      List<Tag> tagList = data.tagList().stream()
          .map(name -> tagRepository.findByName(name)
              .orElseGet(() -> tagRepository.save(Tag.builder().name(name).build())))
          .toList();
      article.setTagList(tagList);
    }

    Article savedArticle = articleRepository.save(article);

    return ArticleResponse.from(savedArticle, article.getAuthor(), false, false, 0);
  }

  public ArticleResponse getArticleBySlug(String slug, String currentUserId) {
    Article article = articleRepository.findBySlug(slug)
        .orElseThrow(ArticleNotFoundException::new);
    User author = article.getAuthor();

    int favoritesCount = favoriteRepository.countByArticleId(article.getId());

    boolean following = false;
    boolean favorited = false;

    if (currentUserId != null && !currentUserId.trim().isEmpty()) {
      Long userId = Long.parseLong(currentUserId);
      User currentUser = userRepository.findById(userId)
          .orElseThrow(UserNotFoundException::new);

      following = followRepository.existsByFollowerIdAndFollowingId(currentUser.getId(), author.getId());
      favorited = favoriteRepository.existsByUserIdAndArticleId(currentUser.getId(), article.getId());
    }

    return ArticleResponse.from(article, author, following, favorited, favoritesCount);
  }

  public TagResponse getTags() {
    List<Tag> tags = tagRepository.findAll();
    return TagResponse.from(tags);
  }

  public ArticlesResponse getArticles(ArticleFilter filter, String currentUserId) {
    Specification<Article> spec = Specification
        .where(ArticleSpecification.hasTag(filter.tag()))
        .and(ArticleSpecification.hasAuthor(filter.author()))
        .and(ArticleSpecification.favoritedBy(filter.favorited()));

    Pageable pageable = PageRequest.of(
        filter.offset() / filter.limit(),
        filter.limit(),
        Sort.by(Sort.Direction.DESC, "createdAt"));

    List<Article> articles = articleRepository.findAll(spec, pageable).getContent();

    User currentUser = currentUserId != null
        ? userRepository.findById(Long.parseLong(currentUserId)).orElse(null)
        : null;

    List<ArticleResponse.ArticleData> art = articles.stream()
        .map(article -> {
          User author = article.getAuthor();

          boolean following = currentUser != null &&
              followRepository.existsByFollowerIdAndFollowingId(
                  currentUser.getId(), author.getId());
          boolean favorited = currentUser != null &&
              favoriteRepository.existsByUserIdAndArticleId(
                  currentUser.getId(), article.getId());
          int favoritesCount = favoriteRepository.countByArticleId(article.getId());
          return ArticleResponse.from(article, author, following, favorited, favoritesCount).article();
        })
        .toList();

    return new ArticlesResponse(art, art.size());

  }

  public static String toSlug(String input) {
    if (input == null)
      return null;

    String slug = Normalizer.normalize(input, Normalizer.Form.NFD)
        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
        .toLowerCase(Locale.ENGLISH)
        .replaceAll("[^a-z0-9\\s-]", "")
        .trim()
        .replaceAll("\\s+", "-")
        .replaceAll("-{2,}", "-");

    return slug;
  }

}
