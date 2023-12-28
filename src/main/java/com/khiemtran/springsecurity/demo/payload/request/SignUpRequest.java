package com.khiemtran.springsecurity.demo.payload.request;

import com.khiemtran.springsecurity.demo.model.User;
import com.khiemtran.springsecurity.demo.security.Sanitizer;
import com.khiemtran.springsecurity.demo.utils.SanitizerUtils;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignUpRequest implements Sanitizer<SignUpRequest> {
  @NotBlank
  private String name;
  @NotBlank
  private String username;
  @NotBlank
  @Email
  private String email;
  @NotBlank
  private String password;

  public User toEntity() {
    User user = new User();
    user.setName(name);
    user.setUsername(username);
    user.setEmail(email);
    user.setPassword(password);
    return user;
  }

  @Override
  public SignUpRequest sanitize(SignUpRequest signUpRequest) {
    SignUpRequest sanitizer = new SignUpRequest();
    sanitizer.name = SanitizerUtils.sanitizeString(signUpRequest.name);
    sanitizer.username = SanitizerUtils.sanitizeString(sanitizer.username);
    sanitizer.email = SanitizerUtils.sanitizeString(sanitizer.email);
    sanitizer.password = SanitizerUtils.sanitizeString(sanitizer.password);
    return sanitizer;
  }
}
