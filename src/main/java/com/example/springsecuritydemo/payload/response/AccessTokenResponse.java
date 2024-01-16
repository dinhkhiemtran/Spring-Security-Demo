package com.example.springsecuritydemo.payload.response;

import lombok.Data;

@Data
public class AccessTokenResponse {
  private String accessToken;
  private String tokenType = "Bearer";

  public AccessTokenResponse(String accessToken) {
    this.accessToken = accessToken;
  }
}
