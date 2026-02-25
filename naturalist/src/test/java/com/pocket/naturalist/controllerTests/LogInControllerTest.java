package com.pocket.naturalist.controllerTests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.pocket.naturalist.controller.LogInController;
import com.pocket.naturalist.repository.UserRepository;
import com.pocket.naturalist.security.JwtService;
import com.pocket.naturalist.security.ParkSecurity;

@WebMvcTest(LogInController.class)
class LogInControllerTest {

    @Autowired
	private MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;
    @MockitoBean
    private JwtService jwtService;

    @MockitoBean(name = "parkSecurity") 
    private ParkSecurity parkSecurity;

    
    @MockitoBean
    private UserRepository userRepository; 
    
}
