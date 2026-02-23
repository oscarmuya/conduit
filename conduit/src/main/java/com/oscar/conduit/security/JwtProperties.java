package com.oscar.conduit.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("security.jwt")
public class JwtProperties {

  private String secret;
  private Long expiration;

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public void setExpiration(Long expiration) {
    this.expiration = expiration;
  }

  public String getSecret() {
    return secret;
  }

  public Long getExpiration() {
    return expiration;
  }
}
