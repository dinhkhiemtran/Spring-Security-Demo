package com.khiemtran.springsecurity.demo.service.imp;

import com.khiemtran.springsecurity.demo.model.Role;
import com.khiemtran.springsecurity.demo.model.RoleName;
import com.khiemtran.springsecurity.demo.model.User;
import com.khiemtran.springsecurity.demo.payload.request.LoginRequest;
import com.khiemtran.springsecurity.demo.payload.request.SignUpRequest;
import com.khiemtran.springsecurity.demo.payload.response.ApiResponse;
import com.khiemtran.springsecurity.demo.provider.JwtProvider;
import com.khiemtran.springsecurity.demo.repository.RoleRepository;
import com.khiemtran.springsecurity.demo.repository.UserRepository;
import com.khiemtran.springsecurity.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;

@Service
public class UserServiceImp implements UserService {
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final AuthenticationManager authenticationManager;
  private final JwtProvider jwtProvider;

  public UserServiceImp(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository, JwtProvider jwtProvider) {
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtProvider = jwtProvider;
  }

  @Override
  public ResponseEntity<?> createUser(SignUpRequest signUpRequest) {
    User user = signUpRequest.toEntity();
    user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
    Role userRole = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow();
    user.setRoles(Collections.singleton(userRole));
    User result = userRepository.save(user);
    URI location = ServletUriComponentsBuilder
        .fromCurrentRequest().path("/api/users/{username}")
        .buildAndExpand(result.getName()).toUri();
    return ResponseEntity.created(location).body(new ApiResponse("User created successfully."));
  }

  @Override
  public String login(LoginRequest sanitizer) {
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
        sanitizer.getEmail(),
        sanitizer.getPassword()
    );
    Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authentication);
    SecurityContextHolder.setContext(context);
    return jwtProvider.generateJwtToken(authentication);
  }

  private Object getPrincipal() {
    SecurityContext context = SecurityContextHolder.getContext();
    Authentication authentication = context.getAuthentication();
    return authentication.getPrincipal();
  }
}
