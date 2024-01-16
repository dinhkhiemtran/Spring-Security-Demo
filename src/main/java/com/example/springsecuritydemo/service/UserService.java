package com.example.springsecuritydemo.service;

import com.example.springsecuritydemo.exception.ExceptionResponse;
import com.example.springsecuritydemo.payload.request.LoginRequest;
import com.example.springsecuritydemo.payload.request.SignUpRequest;
import com.example.springsecuritydemo.payload.response.UserResponse;

import java.util.List;

public interface UserService {
  UserResponse registerUser(SignUpRequest sanitizer) throws ExceptionResponse;

  String getAccessToken(LoginRequest sanitizerLoginRequest);

  List<UserResponse> getAllUsers();
}
