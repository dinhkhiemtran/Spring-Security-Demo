package com.khiemtran.springsecurity.demo.payload.request;

import com.khiemtran.springsecurity.demo.security.Sanitizer;
import com.khiemtran.springsecurity.demo.utils.SanitizerUtils;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest implements Sanitizer<LoginRequest> {
  @NotBlank(message = "Email is not null or empty.")
  @Email(message = "Email is not valid")
  private String email;
  @NotBlank(message = "Password is not null.")
  private String password;

  @Override
  public LoginRequest sanitize(LoginRequest loginRequest) {
    LoginRequest sanitizer = new LoginRequest();
    sanitizer.email = SanitizerUtils.sanitizeString(loginRequest.email);
    sanitizer.password = SanitizerUtils.sanitizeString(loginRequest.password);
    return sanitizer;
  }
}
