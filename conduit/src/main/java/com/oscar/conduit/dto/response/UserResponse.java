package com.oscar.conduit.dto.response;

import com.oscar.conduit.user.User;

public record UserResponse(UserData user) {
  public record UserData(
      String email,
      String token,
      String username,
      String bio,
      String image) {
  }

  public static UserResponse from(String token, User fromUser) {
    UserData userData = new UserData(fromUser.getEmail(), token, fromUser.getUsername(), fromUser.getBio(),
        fromUser.getImage());
    return new UserResponse(userData);
  }
}
