package com.oscar.conduit.article.dto.response;

import java.util.List;

import com.oscar.conduit.article.Tag;

public record TagResponse(
    List<String> tags) {

  public static TagResponse from(List<Tag> tags) {
    return new TagResponse(tags.stream().map(Tag::getName).toList());
  }
}
