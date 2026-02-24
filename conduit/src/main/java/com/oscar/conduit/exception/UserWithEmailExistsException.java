package com.oscar.conduit.exception;

public class UserWithEmailExistsException extends RuntimeException {

  public UserWithEmailExistsException(String email) {
    super("User with email " + email + " already exists");
  }
}
