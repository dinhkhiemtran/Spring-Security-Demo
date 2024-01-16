package com.example.springsecuritydemo.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = false)
public class ExceptionResponse extends Exception {
  private String message;
  private HttpStatus httpStatus;

  public ExceptionResponse(String message) {
    this.message = message;
  }

  public void setStatus(String status) {
    this.httpStatus = HttpStatus.valueOf(Integer.parseInt(status));
  }
}
