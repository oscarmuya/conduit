package com.oscar.conduit.follow;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oscar.conduit.dto.response.ProfileResponse;

import jakarta.annotation.Nullable;

@RestController
@RequestMapping("/api/profiles")
public class FollowController {

  private final FollowService followService;

  public FollowController(FollowService followService) {
    this.followService = followService;
  }

  @GetMapping("/{username}")
  public ResponseEntity<ProfileResponse> getProfile(@Nullable Authentication authentication,
      @PathVariable String username) {

    String currentUser = Optional.ofNullable(authentication)
        .map(Authentication::getName)
        .orElse(null);
    return ResponseEntity.ok(followService.getProfile(currentUser, username));
  }

  @PostMapping("/{username}/follow")
  public ResponseEntity<ProfileResponse> followUser(Authentication authentication, @PathVariable String username) {
    return ResponseEntity.ok(followService.followUser(authentication.getName(), username));
  }

  @DeleteMapping("/{username}/follow")
  public ResponseEntity<ProfileResponse> unfollowUser(Authentication authentication, @PathVariable String username) {
    return ResponseEntity.ok(followService.unfollowUser(authentication.getName(), username));
  }

}
