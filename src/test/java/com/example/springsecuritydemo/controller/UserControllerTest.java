package com.example.springsecuritydemo.controller;

import com.example.springsecuritydemo.payload.response.UserResponse;
import com.example.springsecuritydemo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest {
  private static final String PATH = "/api/v1";
  private final UserService userService = mock(UserService.class);
  private MockMvc mockMvc = mock(MockMvc.class);

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
    UserController userController = new UserController(userService);
    this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
  }

  @Test
  @DisplayName("Get all users")
  public void test() throws Exception {
    UserResponse userResponse = new UserResponse("username", "name", "email@mail");
    UserResponse userResponse1 = new UserResponse("username1", "name1", "email1@mail");
    UserResponse userResponse2 = new UserResponse("username2", "name2", "email2@mail");
    when(userService.getAllUsers()).thenReturn(List.of(userResponse, userResponse1, userResponse2));
    List<UserResponse> expect = List.of(userResponse, userResponse1, userResponse2);
    mockMvc.perform(get(PATH + "/user"))
        .andExpect(status().isOk())
        .andExpect(content().string(new ObjectMapper().writeValueAsString(expect)));
  }
}