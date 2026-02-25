package com.oscar.conduit.article.dto.response;

import java.util.List;

public record ArticlesResponse(
    List<ArticleResponse.ArticleData> articles,
    int articlesCount) {

}
