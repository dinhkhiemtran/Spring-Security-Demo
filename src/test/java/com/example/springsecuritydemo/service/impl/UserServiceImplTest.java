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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
  private UserServiceImpl userServiceImpl;
  private final UserRepository userRepository = mock(UserRepository.class);
  private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
  private final RoleRepository roleRepository = mock(RoleRepository.class);
  private final SignUpRequest signUpRequest = mock(SignUpRequest.class);
  private final User user = mock(User.class);
  private final Role role = mock(Role.class);
  private final UserResponse userResponse = mock(UserResponse.class);
  private final AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
  private final JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
  private final LoginRequest loginRequest = mock(LoginRequest.class);
  private final Authentication authentication = mock(Authentication.class);

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
    userServiceImpl = new UserServiceImpl(authenticationManager, userRepository, roleRepository, passwordEncoder, jwtTokenProvider);
    when(signUpRequest.getEmail()).thenReturn("email@gmail.com");
    when(signUpRequest.getName()).thenReturn("name");
    when(signUpRequest.getPassword()).thenReturn("password");
    when(signUpRequest.getUsername()).thenReturn("username");
  }

  @Test
  @DisplayName("Register User")
  public void testRegisterUser() throws ExceptionResponse {
    when(user.getEmail()).thenReturn("email@gmail.com");
    when(user.getName()).thenReturn("name");
    when(user.getUsername()).thenReturn("username");
    when(user.getPassword()).thenReturn("passwordEncoded");
    when(signUpRequest.toEntity()).thenReturn(user);
    when(passwordEncoder.encode(anyString())).thenReturn("passwordEncoded");
    when(role.getName()).thenReturn(RoleName.ROLE_USER);
    when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(Optional.of(role));
    when(userResponse.getUsername()).thenReturn("username");
    when(userResponse.getEmail()).thenReturn("email@gmail.com");
    when(userResponse.getName()).thenReturn("name");
    when(user.toResponse()).thenReturn(userResponse);
    var actual = userServiceImpl.registerUser(signUpRequest);
    Assertions.assertEquals("name", actual.getName());
    Assertions.assertEquals("username", actual.getUsername());
    Assertions.assertEquals("email@gmail.com", actual.getEmail());
  }

  @Test
  @DisplayName("Exists By Email")
  public void testExistsByEmail() throws ExceptionResponse {
    when(user.getEmail()).thenReturn("email@gmail.com");
    when(user.getName()).thenReturn("name");
    when(user.getUsername()).thenReturn("username");
    when(user.getPassword()).thenReturn("passwordEncoded");
    when(signUpRequest.toEntity()).thenReturn(user);
    when(passwordEncoder.encode(anyString())).thenReturn("passwordEncoded");
    when(role.getName()).thenReturn(RoleName.ROLE_USER);
    when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(Optional.of(role));
    when(userRepository.existsByEmail(anyString())).thenReturn(true);
    var actual = userServiceImpl.registerUser(signUpRequest);
    Assertions.assertNull(actual);
  }

  @Test
  @DisplayName("Exception Response")
  public void testExceptionResponse() {
    when(user.getEmail()).thenReturn("email@gmail.com");
    when(user.getName()).thenReturn("name");
    when(user.getUsername()).thenReturn("username");
    when(user.getPassword()).thenReturn("passwordEncoded");
    when(signUpRequest.toEntity()).thenReturn(user);
    when(passwordEncoder.encode(anyString())).thenReturn("passwordEncoded");
    var exception = Assertions.assertThrows(ExceptionResponse.class, () -> {
      userServiceImpl.registerUser(signUpRequest);
    });
    Assertions.assertEquals("User Role not set.", exception.getMessage());
  }

  @Test
  @DisplayName("Get Access Token")
  public void testGetAccessToken() {
    when(loginRequest.getEmail()).thenReturn("email@mail.com");
    when(loginRequest.getPassword()).thenReturn("password");
    when(loginRequest.getUsername()).thenReturn("username");
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
    when(jwtTokenProvider.generateJwt(any(Authentication.class))).thenReturn("testJwt");
    String actual = userServiceImpl.getAccessToken(loginRequest);
    Assertions.assertEquals("testJwt", actual);
  }

  @Test
  @DisplayName("Test find all users")
  public void testGetAllUsers() {
    User user = new User("name",
        "username",
        "testEmail@mail.com",
        "password");
    when(userRepository.findAll()).thenReturn(List.of(user));
    List<UserResponse> actual = userServiceImpl.getAllUsers();
    Assertions.assertEquals(actual.size(), 1);
    Assertions.assertEquals(actual.stream().map(UserResponse::getUsername).findFirst().orElse(null), "username");
    Assertions.assertEquals(actual.stream().map(UserResponse::getName).findFirst().orElse(null), "name");
    Assertions.assertEquals(actual.stream().map(UserResponse::getEmail).findFirst().orElse(null), "testEmail@mail.com");
  }
}