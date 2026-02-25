package com.oscar.conduit.article.dto.request;

public record ArticleFilter(
    String tag,
    String author,
    String favorited,
    int limit,
    int offset) {
}
