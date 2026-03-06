package com.pocket.naturalist.controllerTests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.n52.jackson2.datatype.jts.JtsModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pocket.naturalist.controller.AdminController;
import com.pocket.naturalist.dto.ParkDataDTO;
import com.pocket.naturalist.entity.Animal;
import com.pocket.naturalist.entity.Park;
import com.pocket.naturalist.entity.User;
import com.pocket.naturalist.entity.Enums.Role;
import com.pocket.naturalist.security.JwtService;
import com.pocket.naturalist.security.ParkSecurity;
import com.pocket.naturalist.service.ParkService;

@WebMvcTest(controllers = AdminController.class)
@EnableMethodSecurity
class AdminControllerTest {
    // Actions required by admin controller
    // 1. Add feature to a park POST
    // 2. Set the boundary polygon for the park POST
    // 3. Retrieve park data for analysis and visualization GET
    // 4. Add another admin POST 
    // 5. Send park data to show current values in the park editing form

    ObjectMapper objectMapper;


    @MockitoBean(name="parkSecurity")
    ParkSecurity parkSecurity;

    @MockitoBean
    ParkService parkService;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
	private MockMvc mockMvc;

    @MockitoBean
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
        adminUser.setRole(Role.ADMIN);
        adminUser.setPassword("password");
        adminUser.setUsername("testuser@test.com");
        this.objectMapper = new ObjectMapper().registerModule(new JtsModule());

    }



    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldAllowAdminToRetrieveCurrentParkData() throws Exception{
        ParkDataDTO data = new ParkDataDTO(
                                park.getName(), 
                                park.getBoundaryList(), 
                                park.getFeatures(),  
                                park.getAnimals()
                            );

        when(parkService.getAdminParkData(parkSlug)).thenReturn(data);
        when(parkSecurity.isParkAdmin(any(), eq(parkSlug))).thenReturn(true);


        this.mockMvc.perform(get("/admin/park/" + parkSlug + "/info"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "hacker", roles = "USER")
    void shouldRejectAccessForNonAdminUser() throws Exception{
        ParkDataDTO data = new ParkDataDTO(
                                park.getName(), 
                                park.getBoundaryList(), 
                                park.getFeatures(),  
                                park.getAnimals()
                            );

        when(parkService.getAdminParkData(parkSlug)).thenReturn(data);
        when(parkSecurity.isParkAdmin(any(), eq(parkSlug))).thenReturn(false);

        this.mockMvc.perform(get("/admin/park/" + parkSlug + "/info"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN" )
    void shouldAllowAdminToUpdateParkData() throws Exception{
        Park updatedPark = new Park("Updated Park");
        updatedPark.setBoundaryList(park.getBoundaryList());
        updatedPark.setFeatures(park.getFeatures());
        updatedPark.setAnimals(park.getAnimals());
        ParkDataDTO data = new ParkDataDTO("Updated Park", park.getBoundaryList(), park.getFeatures(), park.getAnimals());

        String jsonData = objectMapper.writeValueAsString(data);
        when(parkService.updateParkData(eq("test-park"), any(ParkDataDTO.class))).thenReturn(data);
        when(parkSecurity.isParkAdmin(any(), eq(parkSlug))).thenReturn(true);


        this.mockMvc.perform(post("/admin/park/" + parkSlug + "/update")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonData));
    }


    

}
