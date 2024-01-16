package com.example.springsecuritydemo.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(NullPointerException.class)
  @ResponseStatus(HttpStatus.BAD_GATEWAY)
  public ResponseEntity<?> nullPointerExceptionHandler(NullPointerException nullPointerException) {
    log.error(nullPointerException.getMessage());
    return ResponseEntity.badRequest().body(nullPointerException.getMessage());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<?> illegalArgumentExceptionHandler(IllegalArgumentException illegalArgumentException) {
    log.error(illegalArgumentException.getMessage());
    return ResponseEntity.badRequest().body(illegalArgumentException.getMessage());
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ResponseEntity<?> usernameNotFoundExceptionHandler(UsernameNotFoundException usernameNotFoundException) {
    log.error(usernameNotFoundException.getMessage());
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<?> internalServerErrorExceptionHandler(Exception exception) {
    log.error(exception.getMessage());
    return ResponseEntity.internalServerError().body(exception.getMessage());
  }
}
