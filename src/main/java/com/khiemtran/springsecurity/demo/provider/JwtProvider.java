package com.khiemtran.springsecurity.demo.provider;

import com.khiemtran.springsecurity.demo.authentication.UserPrincipal;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class JwtProvider {
  @Value("${spring.security.app.jwtExpirationInMs}")
  private String jwtExpirationInMs;
  @Value("${spring.security.app.jwtSecret}")
  private String jwtSecret;

  public String generateJwtToken(Authentication authentication) {
    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
    return Jwts.builder()
        .setSubject(String.valueOf(userPrincipal.getId()))
        .setAudience(userPrincipal.getUsername())
        .setIssuer(userPrincipal.getEmail())
        .setExpiration(getExpireTime())
        .signWith(getSigningKey(),SignatureAlgorithm.HS256)
//        .signWith(getSignatureAlgorithm())
        .compact();
  }

  private Date getExpireTime() {
    Date now = new Date();
    return new Date(now.getTime() + Long.parseLong(jwtExpirationInMs));
  }

  private Key getSignatureAlgorithm() {
    byte[] keyBytes = getSigningKeyFromApplicationConfiguration().getEncoded();
    return Keys.hmacShaKeyFor(keyBytes);
  }

  protected SecretKey getSigningKeyFromApplicationConfiguration() {
    return Keys.secretKeyFor(SignatureAlgorithm.HS256);
  }

  private Key getSigningKey() {
    byte[] keyBytes = this.jwtSecret.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  protected String getDecode() {
    byte[] decodedBytes = Base64.getDecoder().decode(jwtSecret);
    return new String(decodedBytes);
  }

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
