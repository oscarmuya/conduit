package com.oscar.conduit.user.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
    @NotNull(message = "user object is required") @Valid User user) {
  public record User(
      @Email(message = "invalid email format") String email,
      @Size(min = 3, max = 10) String username,
      String password,
      String image,
      String bio) {
  }
}
