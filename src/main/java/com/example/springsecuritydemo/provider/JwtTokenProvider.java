package com.example.springsecuritydemo.provider;

import com.example.springsecuritydemo.security.UserPrincipal;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {
  @Value("${spring.security.app.jwtExpirationInMs}")
  private String jwtExpirationInMs;
  @Value("${spring.security.app.jwtSecret}")
  private String jwtSecret;

  public String generateJwt(Authentication authentication) {
    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
    return Jwts.builder()
        .setSubject(String.valueOf(userPrincipal.getId()))
        .setAudience(userPrincipal.getUsername())
        .setIssuer(userPrincipal.getEmail())
        .setExpiration(getExpireTime())
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  private Date getExpireTime() {
    Date now = new Date();
    return new Date(now.getTime() + Long.parseLong(jwtExpirationInMs));
  }

  private Key getSigningKey() {
    byte[] keyBytes = this.jwtSecret.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public boolean validateJwt(String jwt) {
    try {
      Jwts
          .parserBuilder()
          .setSigningKey(getSigningKey())
          .build()
          .parseClaimsJws(jwt);
      return true;
    } catch (SignatureException ex) {
      log.error("Invalid JWT signature");
    } catch (MalformedJwtException ex) {
      log.error("Invalid JWT token");
    } catch (ExpiredJwtException ex) {
      log.error("Expired JWT token");
    } catch (UnsupportedJwtException ex) {
      log.error("Unsupported JWT token");
    } catch (IllegalArgumentException ex) {
      log.error("JWT claims string is empty.");
    }
    return false;
  }

  public Claims getClaimsJwt(String jwt) {
    return Jwts
        .parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(jwt)
        .getBody();
  }
//  private Key getSignatureAlgorithm() {
//    byte[] keyBytes = getSigningKeyFromApplicationConfiguration().getEncoded();
//    return Keys.hmacShaKeyFor(keyBytes);
//  }
//
//  protected SecretKey getSigningKeyFromApplicationConfiguration() {
//    return Keys.secretKeyFor(SignatureAlgorithm.HS256);
//  }

  protected static void randomJwtSecretEncoded() {
    int keyLength = 64;
    byte[] secretKeyBytes = new byte[keyLength];
    SecureRandom secureRandom = new SecureRandom();
    secureRandom.nextBytes(secretKeyBytes);
    String secretKey = java.util.Base64.getEncoder().encodeToString(secretKeyBytes);
    System.out.println("Generated Secret Key: " + secretKey);
  }

  public static void main(String[] args) {
    randomJwtSecretEncoded();
  }
}
