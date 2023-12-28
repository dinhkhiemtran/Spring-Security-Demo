package com.khiemtran.springsecurity.demo.service;

import com.khiemtran.springsecurity.demo.payload.request.SignUpRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {
  ResponseEntity<?> createUser(SignUpRequest signUpRequest);
}
