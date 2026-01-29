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

import org.springframework.http.MediaType;
import org.springframework.http.codec.cbor.Jackson2CborDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.Module;
import com.pocket.naturalist.controller.SightingController;
import com.pocket.naturalist.dto.AnimalLocationsDTO;
import com.pocket.naturalist.dto.SightingMapDTO;
import com.pocket.naturalist.dto.SightingReportDTO;
import com.pocket.naturalist.entity.Animal;
import com.pocket.naturalist.entity.Park;
import com.pocket.naturalist.service.SightingService;

import tools.jackson.databind.JacksonModule;


@WebMvcTest(controllers = SightingController.class)
public class SightingControllerTest {
    private GeometryFactory geometryFactory;

    @Autowired
    WebApplicationContext webApplicationContext;

    // @TestConfiguration
    // static class TestConfig {
    //     @Bean
    //     public JtsModule jtsModule() {
    //         return new JtsModule();
    //     }
    // }

    private Animal animal;
    private Point locationOfAnimal;
    private Park park;

    @MockitoBean
    private SightingService sightingService;

    @Autowired
	private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        geometryFactory = new GeometryFactory();
        animal = new Animal("Bison bison", "Bison", "Large herbivorous mammal native to North America.");
        locationOfAnimal = geometryFactory.createPoint(new Coordinate(-110.5885, 44.4279));
        park = new Park("Yellowstone");
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

    }


    @Test
    public void getRequestShouldReturnSightingDtoList() throws Exception {
        String parkSlug = park.getURLSlug();

        when(sightingService.getSightingsForPark(parkSlug))
        .thenReturn(new SightingMapDTO(parkSlug, List.of(new AnimalLocationsDTO(animal.getCommonName(), List.of(locationOfAnimal)))));

        this.mockMvc.perform(get("/sightings/" + parkSlug))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.park").value(parkSlug))
            .andExpect(jsonPath("$.animalLocations").isArray());

    }

    @Test
    public void shouldReturn404ForUnknownPark() throws Exception {
        String unknownParkSlug = "unknown-park";

        when(sightingService.getSightingsForPark(unknownParkSlug))
        .thenReturn(new SightingMapDTO(unknownParkSlug, List.of()));

        this.mockMvc.perform(get("/sightings/" + unknownParkSlug))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.park").value(unknownParkSlug))
            .andExpect(jsonPath("$.animalLocations").isArray())
            .andExpect(jsonPath("$.animalLocations").isEmpty());
    }

    @Test
    public void shouldReturn400ForInvalidParkSlug() throws Exception {
        String invalidParkSlug = "invalid/park";

        this.mockMvc.perform(get("/sightings/" + invalidParkSlug))
            .andExpect(status().isNotFound());
    }

    @Test
    public void shouldAllowCreationOfSighting() throws Exception {
        String parkSlug = park.getURLSlug();
        String animalName = "Bison";
        SightingReportDTO sightingReportDTO = new SightingReportDTO(
            animalName,
            locationOfAnimal,
            locationOfAnimal
        );

        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JtsModule());
        String sightingJson = objectMapper.writeValueAsString(sightingReportDTO);
        System.out.println("Sighting Report DTO: " + sightingReportDTO.toString());

        when(sightingService.createSighting(animalName, locationOfAnimal, locationOfAnimal, parkSlug))
        .thenReturn(true);

        this.mockMvc.perform(post("/sightings/" + parkSlug).content(sightingJson).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    
}
