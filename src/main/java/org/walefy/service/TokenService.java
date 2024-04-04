package org.walefy.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
  private final Algorithm algorithm;

  public TokenService(@Value("${api.security.token.secret}") String secret) {
    this.algorithm = Algorithm.HMAC256(secret);
  }

  public String generateToken(String email) {
    return JWT.create()
        .withSubject(email)
        .withExpiresAt(this.generateExpiration())
        .sign(algorithm);
  }

  public String validateToken(String token) {
    return JWT.require(algorithm)
        .build()
        .verify(token)
        .getSubject();
  }

  private Instant generateExpiration() {
    return Instant.now().plus(15, ChronoUnit.DAYS);
  }
}
