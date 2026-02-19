package com.pocket.naturalist.controllerTests;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.n52.jackson2.datatype.jts.JtsModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pocket.naturalist.controller.CheckInController;
import com.pocket.naturalist.dto.CheckInResponseDTO;
import com.pocket.naturalist.dto.CheckInResponseFeatureDTO;
import com.pocket.naturalist.entity.Park;
import com.pocket.naturalist.repository.UserRepository;
import com.pocket.naturalist.security.JwtService;
import com.pocket.naturalist.security.ParkSecurity;
import com.pocket.naturalist.service.LocationService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CheckInController.class)
class CheckInControllerTest {
    Point userLocation;
    Polygon parkBoundary;
    Park park;


    GeometryFactory geometryFactory;
    ObjectMapper objectMapper;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean(name = "parkSecurity") 
    private ParkSecurity parkSecurity;

    
    @MockitoBean
    private UserRepository userRepository; 

    @MockitoBean
    LocationService locationService;

    @Autowired
	private MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @SuppressWarnings("removal")
    @BeforeEach
    void setUp() {
        geometryFactory = new GeometryFactory();
        parkBoundary = geometryFactory.createPolygon(geometryFactory.createLinearRing(new Coordinate[] {
                new Coordinate(0, 0),
                new Coordinate(2, 0),
                new Coordinate(2, 2),
                new Coordinate(0, 2),
                new Coordinate(0, 0)
        }), null);  
        park = new Park("Test Park");
        park.addBoundary(parkBoundary);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.objectMapper = new ObjectMapper().registerModule(new JtsModule());
    }
    


    @Test
    void checkInShouldReturnPositiveWhenUserInsideParkBoundary() throws Exception {
        userLocation = geometryFactory.createPoint(new Coordinate(1, 1));
        when(locationService.isPointInsideParkBoundaries(userLocation, park.getUrlSlug())).thenReturn(true);

        String jsonLocation = objectMapper.writeValueAsString(userLocation);
        String responseContent = objectMapper.writeValueAsString(new CheckInResponseDTO(park.getUrlSlug(), true));
        this.mockMvc.perform(post("/checkin/{parkSlug}", park.getUrlSlug())
            .content(jsonLocation)
            .contentType("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().string(responseContent));

    }

    @Test
    void checkInShouldReturnNegativeWhenUserOutsideParkBoundary() throws Exception {
        userLocation = geometryFactory.createPoint(new Coordinate(3, 3));
        when(locationService.isPointInsideParkBoundaries(userLocation, park.getUrlSlug())).thenReturn(false);

        String jsonLocation = objectMapper.writeValueAsString(userLocation);
        String responseContent = objectMapper.writeValueAsString(new CheckInResponseDTO(park.getUrlSlug(), false));

        this.mockMvc.perform(post("/checkin/{parkSlug}", park.getUrlSlug())
            .content(jsonLocation)
            .contentType("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().string(responseContent));

    }

    @Test
    void verifyVicinityOfParkFeature() throws Exception {
        userLocation = geometryFactory.createPoint(new Coordinate(1.1, 1.2));
        int featureId = 1;
        when(locationService.isPointNearFeature(userLocation, park.getUrlSlug(), featureId)).thenReturn(true);

        String jsonLocation = objectMapper.writeValueAsString(userLocation);
        String responseContent = objectMapper.writeValueAsString(new CheckInResponseFeatureDTO(park.getUrlSlug(), featureId, true));

        this.mockMvc.perform(post("/checkin/{parkSlug}/feature/{featureId}", park.getUrlSlug(), featureId)
            .content(jsonLocation)
            .contentType("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().string(responseContent));

    }

    @Test
    void verifyNonVicinityOfParkFeature() throws Exception {
        userLocation = geometryFactory.createPoint(new Coordinate(5, 5));
        int featureId = 1;
        when(locationService.isPointNearFeature(userLocation, park.getUrlSlug(), featureId)).thenReturn(false);
        String jsonLocation = objectMapper.writeValueAsString(userLocation);
        String responseContent = objectMapper.writeValueAsString(new CheckInResponseFeatureDTO(park.getUrlSlug(), featureId, false));

        this.mockMvc.perform(post("/checkin/{parkSlug}/feature/{featureId}", park.getUrlSlug(), featureId)
            .content(jsonLocation)
            .contentType("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().string(responseContent));

    }
    
}