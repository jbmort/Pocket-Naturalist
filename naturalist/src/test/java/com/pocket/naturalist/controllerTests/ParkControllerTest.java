package com.pocket.naturalist.controllerTests;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.n52.jackson2.datatype.jts.JtsModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pocket.naturalist.controller.ParkController;
import com.pocket.naturalist.dto.ParkDataDTO;
import com.pocket.naturalist.entity.Animal;
import com.pocket.naturalist.entity.Park;
import com.pocket.naturalist.repository.UserRepository;
import com.pocket.naturalist.security.JwtService;
import com.pocket.naturalist.security.ParkSecurity;
import com.pocket.naturalist.service.ParkService;

@WebMvcTest(controllers = ParkController.class)
class ParkControllerTest {

    Park park;
    GeometryFactory geometryFactory;
    ObjectMapper objectMapper;
    Polygon parkBoundary;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean(name = "parkSecurity") 
    private ParkSecurity parkSecurity;

    
    @MockitoBean
    private UserRepository userRepository; 


    @MockitoBean
    ParkService parkService;

    @Autowired
	private MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @SuppressWarnings("removal")
    @BeforeEach
    void setup(){
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
        park.setAnimals(Set.of(new Animal("Bison bison", "Bison", "Big animal")));
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.objectMapper = new ObjectMapper().registerModule(new JtsModule());
    }

    // main method to retrieve starter park data to populate map on the front end. 
    @Test
    void shouldReturnFullParkData() throws Exception{


        ParkDataDTO parkDataDto = new ParkDataDTO(park.getName(), park.getBoundaryList(), park.getFeatures(), park.getAnimals());
        String responseContent = objectMapper.writeValueAsString(parkDataDto);
        when(parkService.getMainPageParkData(park.getUrlSlug())).thenReturn(parkDataDto);

        this.mockMvc.perform(get("/park/{parkId}", park.getUrlSlug()))
            .andExpect(status().isOk())
            .andExpect(content().json(responseContent));
    }
    
}
