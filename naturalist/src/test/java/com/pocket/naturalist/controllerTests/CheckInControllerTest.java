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
    LocationService locationService;

    @Autowired
	private MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

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
        when(locationService.isPointInsideParkBoundaries(userLocation, park.getURLSlug())).thenReturn(true);

        String jsonLocation = objectMapper.writeValueAsString(userLocation);
        String responseContent = objectMapper.writeValueAsString(new CheckInResponseDTO(park.getURLSlug(), true));
        this.mockMvc.perform(post("/checkin/{parkSlug}", park.getURLSlug())
            .content(jsonLocation)
            .contentType("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().string(responseContent));

    }

    @Test
    void checkInShouldReturnNegativeWhenUserOutsideParkBoundary() throws Exception {
        userLocation = geometryFactory.createPoint(new Coordinate(3, 3));
        when(locationService.isPointInsideParkBoundaries(userLocation, park.getURLSlug())).thenReturn(false);

        String jsonLocation = objectMapper.writeValueAsString(userLocation);
        String responseContent = objectMapper.writeValueAsString(new CheckInResponseDTO(park.getURLSlug(), false));

        this.mockMvc.perform(post("/checkin/{parkSlug}", park.getURLSlug())
            .content(jsonLocation)
            .contentType("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().string(responseContent));

    }

    @Test
    void verifyVicinityOfParkFeature() throws Exception {
        userLocation = geometryFactory.createPoint(new Coordinate(1.1, 1.2));
        int featureId = 1;
        when(locationService.isPointNearFeature(userLocation, park.getURLSlug(), featureId)).thenReturn(true);

        String jsonLocation = objectMapper.writeValueAsString(userLocation);
        String responseContent = objectMapper.writeValueAsString(new CheckInResponseFeatureDTO(park.getURLSlug(), featureId, true));

        this.mockMvc.perform(post("/checkin/{parkSlug}/feature/{featureId}", park.getURLSlug(), featureId)
            .content(jsonLocation)
            .contentType("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().string(responseContent));

    }

    @Test
    void verifyNonVicinityOfParkFeature() throws Exception {
        userLocation = geometryFactory.createPoint(new Coordinate(5, 5));
        int featureId = 1;
        when(locationService.isPointNearFeature(userLocation, park.getURLSlug(), featureId)).thenReturn(false);
        String jsonLocation = objectMapper.writeValueAsString(userLocation);
        String responseContent = objectMapper.writeValueAsString(new CheckInResponseFeatureDTO(park.getURLSlug(), featureId, false));

        this.mockMvc.perform(post("/checkin/{parkSlug}/feature/{featureId}", park.getURLSlug(), featureId)
            .content(jsonLocation)
            .contentType("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().string(responseContent));

    }
    
}