package com.oscar.conduit.auth.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(@NotNull(message = "user object is required") @Valid User user) {
  public record User(
      @NotBlank(message = "email cannot be empty") @Email(message = "invalid email format") String email,
      @NotBlank(message = "username is required") @Size(min = 3, max = 10) String username,
      @NotBlank(message = "password is required") String password) {
  }
}
