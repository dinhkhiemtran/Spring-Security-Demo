package com.khiemtran.springsecurity.demo.payload.response;

import lombok.Data;

@Data
public class JwtTokenResponse {
  private String accessToken;
  private String tokenType = "Bearer";

  public JwtTokenResponse(String accessToken) {
    this.accessToken = accessToken;
  }
}
