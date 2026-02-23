package com.oscar.conduit.auth;

import com.oscar.conduit.security.JwtService;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final JwtService jwtService;

  public AuthController(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  @PostMapping("/login")
  public String handleLogin() {
    String token = jwtService.issue(1L, "oscarmuya@gmail.com", List.of());
    return token;
  }

  @GetMapping("/me")
  public String greeting(Authentication authentication) {
    return "Hello  " + authentication.getName();
  }
}
