package com.example.springsecuritydemo.controller;

import com.example.springsecuritydemo.payload.response.UserResponse;
import com.example.springsecuritydemo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/user")
  public ResponseEntity<?> getAllUsers() {
    List<UserResponse> users = userService.getAllUsers();
    return ResponseEntity.ok(users);
  }
}
