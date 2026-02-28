package com.pocket.naturalist.controllerTests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pocket.naturalist.controller.LogInController;
import com.pocket.naturalist.dto.LoginDTO;
import com.pocket.naturalist.repository.UserRepository;
import com.pocket.naturalist.security.JwtService;
import com.pocket.naturalist.security.ParkSecurity;
import com.pocket.naturalist.security.SecurityConfig;

@WebMvcTest(LogInController.class)
@Import(SecurityConfig.class)
class LogInControllerTest {

    private ObjectMapper objectMapper;

    @Autowired
	private MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockitoBean
    private AuthenticationProvider authenticationProvider;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean(name = "parkSecurity") 
    private ParkSecurity parkSecurity;

    
    @MockitoBean
    private UserRepository userRepository; 

    @BeforeEach
    void setup(){
        this.objectMapper = new ObjectMapper();
    }

    @Test
    @WithMockUser(username = "username", password = "password")
    void shouldAllowUserToLogIn() throws Exception{
        LoginDTO logInCreds = new LoginDTO("username", "password");
        String expectedToken = "eyJhbGciOiJIUzI1NiJ9.mockTokenValue";


        Authentication mockAuth = mock(Authentication.class);
        UserDetails mockUser = new User("username", "password", new ArrayList<>());

        when(mockAuth.getPrincipal()).thenReturn(mockUser);
        when(authenticationManager.authenticate(any())).thenReturn(mockAuth);
        when(jwtService.generateToken(mockUser)).thenReturn(expectedToken);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(logInCreds)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(expectedToken));

    }
    
}
