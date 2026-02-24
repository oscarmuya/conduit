package com.oscar.conduit.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oscar.conduit.dto.response.UserResponse;
import com.oscar.conduit.user.dto.request.UpdateUserRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

  private UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public ResponseEntity<UserResponse> handleGetCurrentUser(Authentication authentication) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(userService.getCurrentUser(Long.parseLong(authentication.getName())));
  }

  @PutMapping
  public ResponseEntity<UserResponse> handleUpdateUser(Authentication authentication,
      @Valid @RequestBody UpdateUserRequest request) {
    return ResponseEntity.ok(userService.updateUser(Long.parseLong(authentication.getName()), request));
  }

}
