package com.oscar.conduit.exception;

public class UnauthorizedException extends RuntimeException {
  public UnauthorizedException() {
    super("Unauthorized access");
  }

}
