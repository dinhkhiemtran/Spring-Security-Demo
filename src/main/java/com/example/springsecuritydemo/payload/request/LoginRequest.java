package com.example.springsecuritydemo.payload.request;

import com.example.springsecuritydemo.security.Sanitizer;
import com.example.springsecuritydemo.utils.SanitizerUtils;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class LoginRequest implements Sanitizer<LoginRequest> {
  @NotBlank
  @Email
  private String email;
  @NotBlank
  private String username;
  @NotBlank
  private String password;

  @Override
  public LoginRequest sanitizer(LoginRequest loginRequest) {
    LoginRequest sanitize = new LoginRequest();
    sanitize.email = SanitizerUtils.sanitizeString(email);
    sanitize.username = SanitizerUtils.sanitizeString(username);
    sanitize.password = SanitizerUtils.sanitizeString(password);
    return sanitize;
  }
}

