package com.pocket.naturalist.controllerTests;

import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.n52.jackson2.datatype.jts.JtsModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pocket.naturalist.controller.SightingController;
import com.pocket.naturalist.dto.AnimalLocationsDTO;
import com.pocket.naturalist.dto.SightingMapDTO;
import com.pocket.naturalist.dto.SightingReportDTO;
import com.pocket.naturalist.entity.Animal;
import com.pocket.naturalist.entity.Park;
import com.pocket.naturalist.repository.UserRepository;
import com.pocket.naturalist.security.JwtService;
import com.pocket.naturalist.security.ParkSecurity;
import com.pocket.naturalist.security.SecurityConfig;
import com.pocket.naturalist.service.SightingService;
import com.pocket.naturalist.exception.ResourceNotFoundException;

@WebMvcTest(controllers = SightingController.class)
@Import(SecurityConfig.class)
class SightingControllerTest {
    private GeometryFactory geometryFactory;
    private Animal animal;
    private Point locationOfAnimal;
    private Park park;

    @Autowired
    private MockMvc mockMvc; 


    // --- SECURITY MOCKS ---
    @MockitoBean private JwtService jwtService;
    @MockitoBean(name = "parkSecurity") private ParkSecurity parkSecurity;
    @MockitoBean private AuthenticationProvider authenticationProvider;
    @MockitoBean private UserDetailsService userDetailsService;
    
    // --- CONTROLLER MOCKS ---
    @MockitoBean private UserRepository userRepository; 
    @MockitoBean private SightingService sightingService;


    @BeforeEach
    void setUp() {
        geometryFactory = new GeometryFactory();
        animal = new Animal("Bison bison", "Bison", "Large herbivorous mammal native to North America.");
        locationOfAnimal = geometryFactory.createPoint(new Coordinate(-110.5885, 44.4279));
        park = new Park("Yellowstone");
    }


    @Test
    @WithMockUser
    void getRequestShouldReturnSightingDtoList() throws Exception {
        String parkSlug = park.getUrlSlug();

        when(sightingService.getSightingsForPark(parkSlug))
        .thenReturn(new SightingMapDTO(parkSlug, List.of(new AnimalLocationsDTO(animal.getCommonName(), List.of(locationOfAnimal)))));

        this.mockMvc.perform(get("/sightings/" + parkSlug))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.parkSlug").value(parkSlug))
            .andExpect(jsonPath("$.animalLocations").isArray());

    }

    @Test
    @WithMockUser
    void shouldReturn404ForUnknownPark() throws Exception {
        String unknownParkSlug = "unknown-park";

        when(sightingService.getSightingsForPark(unknownParkSlug))
        .thenThrow(new ResourceNotFoundException("Park not found"));

        this.mockMvc.perform(get("/sightings/" + unknownParkSlug))
            .andExpect(status().isNotFound());
      
    }

    @Test
    @WithMockUser
    void shouldReturn500ForInvalidParkSlug() throws Exception {
        String invalidParkSlug = "invalid/park";

        this.mockMvc.perform(get("/sightings/" + invalidParkSlug))
            .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser
    void shouldAllowCreationOfSighting() throws Exception {
        String parkSlug = park.getUrlSlug();
        String animalName = "Bison";
        SightingReportDTO sightingReportDTO = new SightingReportDTO(
            animalName,
            locationOfAnimal,
            locationOfAnimal
        );

        @SuppressWarnings("removal")
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JtsModule());
        String sightingJson = objectMapper.writeValueAsString(sightingReportDTO);
        System.out.println("Sighting Report DTO: " + sightingReportDTO.toString());

        when(sightingService.createSighting(animalName, locationOfAnimal, locationOfAnimal, parkSlug))
        .thenReturn(true);

        this.mockMvc.perform(post("/sightings/" + parkSlug).content(sightingJson).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    
}
