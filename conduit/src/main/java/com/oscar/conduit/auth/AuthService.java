package com.oscar.conduit.auth;

import java.util.List;

import org.springframework.stereotype.Service;

import com.oscar.conduit.auth.dto.request.LoginRequest;
import com.oscar.conduit.auth.dto.request.RegisterRequest;
import com.oscar.conduit.dto.response.UserResponse;
import com.oscar.conduit.exception.UserWithEmailExistsException;
import com.oscar.conduit.security.JwtService;
import com.oscar.conduit.user.UserService;
import com.oscar.conduit.user.User;

@Service
public class AuthService {
  private final JwtService jwtService;
  private final UserService userService;

  public AuthService(JwtService jwtService, UserService userService) {
    this.jwtService = jwtService;
    this.userService = userService;
  }

  public UserResponse login(LoginRequest request) {
    User user = userService.login(request);
    String token = jwtService.issue(user.getId(), user.getEmail(), List.of());

    return UserResponse.from(token, user);
  }

  public UserResponse register(RegisterRequest request) {
    boolean exists = userService.userExistsByEmail(request.user().email());
    if (exists) {
      throw new UserWithEmailExistsException(request.user().email());
    }
    User user = userService.create(request);
    String token = jwtService.issue(user.getId(), user.getEmail(), List.of());

    return UserResponse.from(token, user);
  }
}
