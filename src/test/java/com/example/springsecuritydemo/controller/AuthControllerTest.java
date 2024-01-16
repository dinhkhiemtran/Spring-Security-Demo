package com.example.springsecuritydemo.controller;

import com.example.springsecuritydemo.payload.request.LoginRequest;
import com.example.springsecuritydemo.payload.request.SignUpRequest;
import com.example.springsecuritydemo.payload.response.AccessTokenResponse;
import com.example.springsecuritydemo.payload.response.UserResponse;
import com.example.springsecuritydemo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {
  private static final String PATH = "/api/auth";
  private final UserService userService = mock(UserService.class);
  private MockMvc mockMvc = mock(MockMvc.class);
  private final SignUpRequest signUpRequest = mock(SignUpRequest.class);
  private final UserResponse userResponse = mock(UserResponse.class);
  private final LoginRequest loginRequest = mock(LoginRequest.class);
  private final AccessTokenResponse accessTokenResponse = mock(AccessTokenResponse.class);
  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
    AuthController authController = new AuthController(userService);
    this.mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    when(signUpRequest.getName()).thenReturn("name");
    when(signUpRequest.getUsername()).thenReturn("username");
    when(signUpRequest.getPassword()).thenReturn("password");
    when(signUpRequest.getEmail()).thenReturn("email@mail.com");
    when(loginRequest.getUsername()).thenReturn("username");
    when(loginRequest.getPassword()).thenReturn("password");
    when(loginRequest.getEmail()).thenReturn("email@gmail");
  }

  @Test
  @DisplayName("Greeting")
  public void testGreeting() throws Exception {
    mockMvc.perform(get(PATH + "/greeting"))
        .andExpect(status().isOk())
        .andExpect(content().string("Hello World."));
  }

  @Test
  @DisplayName("/sign-up")
  public void testRegister() throws Exception {
    when(signUpRequest.sanitizer(any(SignUpRequest.class))).thenReturn(signUpRequest);
    when(userResponse.getName()).thenReturn("name");
    when(userResponse.getEmail()).thenReturn("email");
    when(userResponse.getUsername()).thenReturn("username");
    when(userService.registerUser(any(SignUpRequest.class))).thenReturn(userResponse);
    var body = objectMapper.writeValueAsString(signUpRequest);
    mockMvc.perform(post(PATH + "/sign-up")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isCreated())
        .andExpect(header().stringValues("Location", "http://localhost/api/users/username"))
        .andExpect(content().string("User created successfully."));
  }

  @Test
  @DisplayName("Bad Request response")
  public void testBadRequestResponse() throws Exception {
    when(signUpRequest.sanitizer(any(SignUpRequest.class))).thenReturn(signUpRequest);
    when(userService.registerUser(any(SignUpRequest.class))).thenReturn(null);
    mockMvc.perform(post(PATH + "/sign-up")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(signUpRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("User created."));
  }

  @Test
  @DisplayName("/sign-in")
  public void testSignIn() throws Exception {
    when(loginRequest.sanitizer(any(LoginRequest.class))).thenReturn(loginRequest);
    when(userService.getAccessToken(any(LoginRequest.class))).thenReturn("testJwt");
    when(accessTokenResponse.getAccessToken()).thenReturn("testJwt");
    when(accessTokenResponse.getTokenType()).thenReturn("Bearer");
    mockMvc.perform(post(PATH + "/sign-in")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(loginRequest)))
        .andExpect(status().isOk())
        .andExpect(content().string(new ObjectMapper().writeValueAsString(accessTokenResponse)));
  }
}