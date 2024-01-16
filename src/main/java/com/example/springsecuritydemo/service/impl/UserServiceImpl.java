package com.example.springsecuritydemo.service.impl;

import com.example.springsecuritydemo.exception.ExceptionResponse;
import com.example.springsecuritydemo.model.Role;
import com.example.springsecuritydemo.model.RoleName;
import com.example.springsecuritydemo.model.User;
import com.example.springsecuritydemo.payload.request.LoginRequest;
import com.example.springsecuritydemo.payload.request.SignUpRequest;
import com.example.springsecuritydemo.payload.response.UserResponse;
import com.example.springsecuritydemo.provider.JwtTokenProvider;
import com.example.springsecuritydemo.repository.RoleRepository;
import com.example.springsecuritydemo.repository.UserRepository;
import com.example.springsecuritydemo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final RoleRepository roleRepository;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;

  public UserServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  public UserResponse registerUser(SignUpRequest sanitizer) throws ExceptionResponse {
    User user = sanitizer.toEntity();
    user.setPassword(passwordEncoder.encode(sanitizer.getPassword()));
    Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
        .orElseThrow(() -> new ExceptionResponse("User Role not set."));
    user.setRoles(Collections.singleton(userRole));
    if (userRepository.existsByEmail(user.getEmail())) {
      return null;
    }
    userRepository.save(user);
    return user.toResponse();
  }

  @Override
  public String getAccessToken(LoginRequest sanitizerLoginRequest) {
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
        sanitizerLoginRequest.getUsername(),
        sanitizerLoginRequest.getPassword()
    );
    Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authentication);
    return jwtTokenProvider.generateJwt(authentication);
  }

  @Override
  public List<UserResponse> getAllUsers() {
    List<User> users = userRepository.findAll();
    return users.stream().map(User::toResponse).toList();
  }
}
