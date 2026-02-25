package com.oscar.conduit.exception;

public class ArticleNotFoundException extends RuntimeException {

  public ArticleNotFoundException() {
    super("Article with given id not found");
  }

}
