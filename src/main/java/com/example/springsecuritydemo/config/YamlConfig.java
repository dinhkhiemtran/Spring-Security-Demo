package com.example.springsecuritydemo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@ConfigurationProperties(prefix = "spring")
@Component
@Data
public class YamlConfig {
  private Security security;

  @Data
  public static class Security {
    List<String> permitEndpoint;
    List<String> corsAllowedOrigins;
    List<String> corsAllowedMethods;

    @Data
    public static class App {
      String jwtSecret;
      String jwtExpirationInMs;
    }
  }
}
