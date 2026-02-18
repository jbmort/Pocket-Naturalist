package com.pocket.naturalist.controllerTests;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.pocket.naturalist.controller.AdminController;
import com.pocket.naturalist.entity.Animal;
import com.pocket.naturalist.entity.Park;
import com.pocket.naturalist.entity.User;
import com.pocket.naturalist.entity.Enums.Role;
import com.pocket.naturalist.security.JwtService;
import com.pocket.naturalist.security.ParkSecurity;
import com.pocket.naturalist.security.SecurityUser;
import com.pocket.naturalist.service.ParkService;

@WebMvcTest(controllers = AdminController.class)
class AdminControllerTest {
    // Actions required by admin controller
    // 1. Add feature to a park POST
    // 2. Set the boundary polygon for the park POST
    // 3. Retrieve park data for analysis and visualization GET
    // 4. Add another admin POST 
    // 5. Send park data to show current values in the park editing form

    @MockitoBean
    ParkSecurity parkSecurity;

    @MockitoBean
    ParkService parkService;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
	private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    String parkSlug;
    User adminUser;
    Park park;

    @BeforeEach
    void setup(){
        park = new Park("Test Park");
        park.setAnimals(Set.of(new Animal("Bison bison", "Bison", "Large herbivorous mammal native to North America.")));
        parkSlug = park.getUrlSlug();
        adminUser = new User();
        adminUser.setRole(Role.ROLE_ADMIN);
        adminUser.setPassword("password");
        adminUser.setUsername("testuser@test.com");
    }



    @Test
    void shouldAllowAdminToRetrieveCurrentParkData() throws Exception{
        Authentication auth = mock(Authentication.class);
        String token = jwtService.generateToken(new SecurityUser(adminUser));

        when(parkService.getAdminParkData(parkSlug)).thenReturn(park);
        when(parkSecurity.isParkAdmin(auth, parkSlug)).thenReturn(true);


        this.mockMvc.perform(get("/admin/park/" + parkSlug + "/info").header("Authorization", "Bearer " + token))
            .andExpect(status().isOk());
    }
    

}
