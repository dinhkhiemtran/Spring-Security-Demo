package com.example.springsecuritydemo.controller;

import com.example.springsecuritydemo.exception.ExceptionResponse;
import com.example.springsecuritydemo.payload.request.LoginRequest;
import com.example.springsecuritydemo.payload.request.SignUpRequest;
import com.example.springsecuritydemo.payload.response.AccessTokenResponse;
import com.example.springsecuritydemo.payload.response.UserResponse;
import com.example.springsecuritydemo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/auth")
public class AuthController {
  private final UserService userService;

  public AuthController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/greeting")
  public ResponseEntity<?> greeting() {
    return ResponseEntity.ok("Hello World.");
  }

  @PostMapping("/sign-up")
  public ResponseEntity<?> register(@RequestBody @Valid SignUpRequest signUpRequest) throws ExceptionResponse {
    SignUpRequest sanitizer = signUpRequest.sanitizer(signUpRequest);
    UserResponse userResponse = userService.registerUser(sanitizer);
    if (userResponse == null) {
      return ResponseEntity.badRequest().body("User created.");
    }
    URI location = ServletUriComponentsBuilder
        .fromCurrentContextPath().path("/api/users/{username}")
        .buildAndExpand(userResponse.getUsername()).toUri();
    return ResponseEntity.created(location).body("User created successfully.");
  }

  @PostMapping("/sign-in")
  public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest) {
    LoginRequest sanitizerLoginRequest = loginRequest.sanitizer(loginRequest);
    String jwt = userService.getAccessToken(sanitizerLoginRequest);
    return ResponseEntity.ok(new AccessTokenResponse(jwt));
  }
}
