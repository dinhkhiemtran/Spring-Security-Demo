package com.example.springsecuritydemo.payload.request;

import com.example.springsecuritydemo.model.User;
import com.example.springsecuritydemo.security.Sanitizer;
import com.example.springsecuritydemo.utils.SanitizerUtils;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SignUpRequest implements Sanitizer<SignUpRequest> {
  @NotBlank(message = "User is not null or empty")
  private String username;
  @NotBlank(message = "Name is not null or empty")
  private String name;
  @NotBlank(message = "Email is not null or empty")
  @Email(message = "Email invalid")
  private String email;
  @NotBlank(message = "Password is not null or empty")
  private String password;

  @Override
  public SignUpRequest sanitizer(SignUpRequest signUpRequest) {
    SignUpRequest sanitizer = new SignUpRequest();
    sanitizer.username = SanitizerUtils.sanitizeString(username);
    sanitizer.name = SanitizerUtils.sanitizeString(name);
    sanitizer.email = SanitizerUtils.sanitizeString(email);
    sanitizer.password = SanitizerUtils.sanitizeString(password);
    return sanitizer;
  }

  public User toEntity() {
    return new User(name, username, email, password);
  }
}
