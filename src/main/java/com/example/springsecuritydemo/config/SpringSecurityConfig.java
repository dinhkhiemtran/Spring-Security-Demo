package com.example.springsecuritydemo.config;

import com.example.springsecuritydemo.exception.JwtAuthenticationEntryPoint;
import com.example.springsecuritydemo.filter.JwtAuthenticationFilter;
import com.example.springsecuritydemo.provider.JwtTokenProvider;
import com.example.springsecuritydemo.security.UserDetailServiceImp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableJpaAuditing
@EnableWebSecurity
@ComponentScan(basePackages = "com.example.springsecuritydemo.config")
public class SpringSecurityConfig {
  private final YamlConfig yamlConfig;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final JwtTokenProvider jwtTokenProvider;
  private final UserDetailServiceImp userDetailServiceImp;

  public SpringSecurityConfig(YamlConfig yamlConfig, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtTokenProvider jwtTokenProvider, UserDetailServiceImp userDetailServiceImp) {
    this.yamlConfig = yamlConfig;
    this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    this.jwtTokenProvider = jwtTokenProvider;
    this.userDetailServiceImp = userDetailServiceImp;
  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    String[] permitEndpoint = yamlConfig.getSecurity().permitEndpoint.toArray(new String[0]);
    return httpSecurity
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth ->
            auth.requestMatchers(permitEndpoint).permitAll()
                .anyRequest().authenticated())
        .exceptionHandling(exceptionHandler ->
            exceptionHandler.authenticationEntryPoint(jwtAuthenticationEntryPoint))
        .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  @Bean
  JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter(jwtTokenProvider, userDetailServiceImp);
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
    return configuration.getAuthenticationManager();
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
}
