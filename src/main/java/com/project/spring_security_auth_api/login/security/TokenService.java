package com.project.spring_security_auth_api.login.security;

import java.security.AlgorithmConstraints;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.project.spring_security_auth_api.login.domain.User;

@Service
public class TokenService {

  @Value("${security.secret.token}")
  private String secret;

  public String generateToken(User user) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);

      String token = JWT.create()
              .withIssuer("login-auth-api")
              .withSubject(user.getEmail())
              .withExpiresAt(generateExpirationTime())
              .sign(algorithm);

      return token;
    } catch (JWTCreationException exception) {
      throw new RuntimeException("Authentication Error");
    }
  }

  public String validateToken(String token) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);

      return JWT.require(algorithm)
              .withIssuer("login-auth-api")
              .build()
              .verify(token)
              .getSubject();
    } catch (JWTVerificationException exception) {
      return null;
    }
  }

  private Instant generateExpirationTime() {
    return LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.of("-03:00")); // SOUTH AMERICA TIME ZONE
  }
}
