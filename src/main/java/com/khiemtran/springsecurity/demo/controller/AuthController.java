package com.khiemtran.springsecurity.demo.controller;

import com.khiemtran.springsecurity.demo.payload.request.LoginRequest;
import com.khiemtran.springsecurity.demo.payload.request.SignUpRequest;
import com.khiemtran.springsecurity.demo.payload.response.JwtTokenResponse;
import com.khiemtran.springsecurity.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final UserService userService;

  public AuthController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/sign-up")
  public ResponseEntity<?> register(@RequestBody @Valid SignUpRequest signUpRequest) {
    SignUpRequest sanitizer = signUpRequest.sanitize(signUpRequest);
    return userService.createUser(sanitizer);
  }

  @PostMapping("/sign-in")
  public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest) {
    LoginRequest sanitizer = loginRequest.sanitize(loginRequest);
    String jwt = userService.login(sanitizer);
    return ResponseEntity.ok(new JwtTokenResponse(jwt));
  }

  @GetMapping("/greeting")
  public ResponseEntity<?> greeting() {
    return ResponseEntity.ok("Hello World!");
  }
}
