package com.khiemtran.springsecurity.demo.controller;

import com.khiemtran.springsecurity.demo.payload.request.SignUpRequest;
import com.khiemtran.springsecurity.demo.service.UserService;
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
  public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
    return userService.createUser(signUpRequest);
  }

  @GetMapping("/greeting")
  public ResponseEntity<?> greeting() {
    return ResponseEntity.ok("Hello World!");
  }
}
