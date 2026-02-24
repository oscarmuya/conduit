package com.oscar.conduit.dto.response;

import com.oscar.conduit.user.User;

public record ProfileResponse(Profile profile) {
  public record Profile(
      String username,
      String bio,
      String image,
      boolean following) {
  }

  public static ProfileResponse from(User fromUser, boolean following) {
    Profile profileData = new Profile(fromUser.getUsername(),
        fromUser.getBio(),
        fromUser.getImage(), following);
    return new ProfileResponse(profileData);
  }
}
