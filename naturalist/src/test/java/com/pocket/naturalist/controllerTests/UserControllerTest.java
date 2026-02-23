package com.pocket.naturalist.controllerTests;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.pocket.naturalist.controller.UserAccountController;
import com.pocket.naturalist.dto.UserDataDto;
import com.pocket.naturalist.entity.Badge;
import com.pocket.naturalist.entity.Park;
import com.pocket.naturalist.entity.User;
import com.pocket.naturalist.entity.UserParkStat;
import com.pocket.naturalist.security.JwtService;
import com.pocket.naturalist.security.ParkSecurity;
import com.pocket.naturalist.security.SecurityConfig;
import com.pocket.naturalist.service.UserService;

@WebMvcTest(controllers = UserAccountController.class)
@Import(SecurityConfig.class)
class UserControllerTest {
    @MockitoBean
    private AuthenticationProvider authenticationProvider;

     @MockitoBean
    private JwtService jwtService;

    @MockitoBean(name = "parkSecurity") 
    private ParkSecurity parkSecurity;

    @Autowired
    WebApplicationContext webApplicationContext;


    @Autowired
	private MockMvc mockMvc;

    @MockitoBean
    UserService userService;

    User user;

    @BeforeEach
    void setUp(){
        user = new User();
        user.setUsername("test User");
        user.setBadges(List.of(new Badge("first badge", "url")));
        Park park = new Park("test park");
        UserParkStat stat = new UserParkStat(user, park);
        stat.addPoints(5);
        user.setUserParkStats(List.of(stat));
    }

    @Test
    @WithMockUser(username = "test user", roles = {"USER"})
    void shouldReturnUserData() throws Exception{
        UserDataDto expected = new UserDataDto("test user", 5, List.of(new Badge("first badge", "url")));
        
        when(userService.getUserInfo("test user")).thenReturn(expected);

        mockMvc.perform(get("/user/data"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalPoints").value(5));
    }

    @Test
    void shouldBlockUserDataWhenNotAuthenticated() throws Exception{        
        mockMvc.perform(get("/user/data"))
            .andExpect(status().is4xxClientError());
    }
}
