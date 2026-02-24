package com.oscar.conduit.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import java.util.List;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

  private static final byte[] KEY_BYTES = new byte[] {
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    0,
  };
  private static final byte[] OTHER_KEY_BYTES = new byte[] {
    1,
    1,
    1,
    1,
    1,
    1,
    1,
    1,
    1,
    1,
    1,
    1,
    1,
    1,
    1,
    1,
    1,
    1,
    1,
    1,
    1,
    1,
    1,
    1,
    1,
    1,
    1,
    1,
    1,
    1,
    1,
    1,
  };
  private static final String SECRET = Encoders.BASE64.encode(KEY_BYTES);
  private static final long EXPIRATION = 3_600_000L;

  @Mock
  private JwtProperties jwtProperties;

  @InjectMocks
  private JwtService jwtService;

  @BeforeEach
  void setUp() {
    when(jwtProperties.getSecret()).thenReturn(SECRET);
  }

  private void stubExpiration() {
    when(jwtProperties.getExpiration()).thenReturn(EXPIRATION);
  }

  @Test
  void issue_shouldReturnNonBlankToken() {
    stubExpiration();
    String token = jwtService.issue(
      1L,
      "user@example.com",
      List.of("ROLE_USER")
    );

    assertThat(token).isNotBlank();
  }

  @Test
  void issue_shouldEmbedEmailAsSubject() {
    stubExpiration();
    String token = jwtService.issue(
      1L,
      "user@example.com",
      List.of("ROLE_USER")
    );
    Claims claims = jwtService.decode(token);

    assertThat(claims.getSubject()).isEqualTo("user@example.com");
  }

  @Test
  void issue_shouldEmbedRolesUnderClaimA() {
    stubExpiration();
    List<String> roles = List.of("ROLE_USER", "ROLE_ADMIN");

    String token = jwtService.issue(1L, "user@example.com", roles);
    Claims claims = jwtService.decode(token);

    List<String> actual = claims.get("a", List.class);
    assertThat(actual).containsExactlyInAnyOrderElementsOf(roles);
  }

  @Test
  void issue_shouldSetExpirationInFuture() {
    stubExpiration();
    String token = jwtService.issue(
      1L,
      "user@example.com",
      List.of("ROLE_USER")
    );
    Claims claims = jwtService.decode(token);

    assertThat(claims.getExpiration()).isInTheFuture();
  }

  @Test
  void decode_shouldReturnValidClaimsForValidToken() {
    stubExpiration();
    String token = jwtService.issue(
      1L,
      "user@example.com",
      List.of("ROLE_USER")
    );

    Claims claims = jwtService.decode(token);

    assertThat(claims).isNotNull();
    assertThat(claims.getSubject()).isEqualTo("user@example.com");
  }

  @Test
  void decode_shouldThrowForTamperedToken() {
    stubExpiration();
    String token = jwtService.issue(
      1L,
      "user@example.com",
      List.of("ROLE_USER")
    );
    String tampered = token.substring(0, token.length() - 4) + "xxxx";

    assertThatThrownBy(() -> jwtService.decode(tampered)).isInstanceOf(
      Exception.class
    );
  }

  @Test
  void decode_shouldThrowForTokenSignedWithDifferentKey() {
    SecretKey differentKey = Keys.hmacShaKeyFor(OTHER_KEY_BYTES);
    String foreignToken = Jwts.builder()
      .subject("attacker@example.com")
      .signWith(differentKey)
      .compact();

    assertThatThrownBy(() -> jwtService.decode(foreignToken)).isInstanceOf(
      Exception.class
    );
  }

  @Test
  void decode_shouldThrowForExpiredToken() {
    when(jwtProperties.getExpiration()).thenReturn(-1000L);

    String expiredToken = jwtService.issue(
      1L,
      "user@example.com",
      List.of("ROLE_USER")
    );

    assertThatThrownBy(() -> jwtService.decode(expiredToken)).isInstanceOf(
      Exception.class
    );
  }
}
