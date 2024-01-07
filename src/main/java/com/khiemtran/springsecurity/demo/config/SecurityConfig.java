package com.khiemtran.springsecurity.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@ComponentScan(basePackages = "com.khiemtran.springsecurity.demo.security")
public class SecurityConfig {
  private final YamlConfig yamlConfig;

  public SecurityConfig(YamlConfig yamlConfig) {
    this.yamlConfig = yamlConfig;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    String[] permitEndpoint = yamlConfig.getSecurity().permitEndpoint.toArray(new String[0]);
    return httpSecurity
        .csrf(AbstractHttpConfigurer::disable)
        .cors((cors) -> cors
            .configurationSource(myWebsiteConfigurationSource())
        )
        .cors(withDefaults())
        .authorizeHttpRequests(auth -> auth.requestMatchers(permitEndpoint).permitAll()
            .anyRequest().authenticated())
        .build();
  }

  private CorsConfigurationSource myWebsiteConfigurationSource() {
    List<String> allowOrigins = yamlConfig.getSecurity().corsAllowedOrigins;
    List<String> allowMethods = yamlConfig.getSecurity().corsAllowedMethods;
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(allowOrigins);
    configuration.setAllowedMethods(allowMethods);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
    return configuration.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
