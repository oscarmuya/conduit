package com.oscar.conduit.auth;

import com.oscar.conduit.auth.dto.request.LoginRequest;
import com.oscar.conduit.auth.dto.request.RegisterRequest;
import com.oscar.conduit.dto.response.UserResponse;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/")
  public ResponseEntity<UserResponse> handleRegister(@Valid @RequestBody RegisterRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
  }

  @PostMapping("/login")
  public ResponseEntity<UserResponse> handleLogin(@Valid @RequestBody LoginRequest request) {
    return ResponseEntity.status(HttpStatus.OK).body(authService.login(request));
  }
}
