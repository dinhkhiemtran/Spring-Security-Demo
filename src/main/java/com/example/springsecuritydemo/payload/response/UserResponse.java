package com.example.springsecuritydemo.payload.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class UserResponse {
  @NotBlank
  private String username;
  @NotBlank
  private String name;
  @NotBlank
  private String email;
}
