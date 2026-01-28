package com.pocket.naturalist.controllerTests;

import static org.mockito.Mockito.when;

import java.util.List;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.client.RestTestClient;

import com.pocket.naturalist.controller.SightingController;
import com.pocket.naturalist.dto.SightingPointDTO;
import com.pocket.naturalist.entity.Animal;
import com.pocket.naturalist.entity.Park;
import com.pocket.naturalist.entity.Sighting;
import com.pocket.naturalist.service.SightingService;

@AutoConfigureRestTestClient
@WebMvcTest(controllers = SightingController.class)
public class SightingControllerTest {
    private GeometryFactory geometryFactory;
    private SightingService sightingService;
    private Animal animal;
    private Point locationOfAnimal;
    private Point locationOfReport;


    @Autowired
	private RestTestClient restTestClient;

    @BeforeEach
    public void setUp() {
        animal = new Animal();
        locationOfAnimal = geometryFactory.createPoint(new Coordinate(-110.5885, 44.4279));
        locationOfReport = geometryFactory.createPoint(new Coordinate(-110.5880, 44.4280));
        park = new Park("Yellowstone");
    }


    @Test
    public void getRequestShouldReturnSightingDtoList() {
        long parkID = 1L;

        when(sightingService.getSightingsForPark(parkID))
        .thenReturn(List.of(new SightingPointDTO(animal, locationOfAnimal, parkID)));

        restTestClient.get().uri("/sightings/park/" + parkID)
            .exchange()
            .expectStatus().isOk()
            .expectBody(List.class)
            .isEqualTo(List.of(new SightingPointDTO(animal, locationOfAnimal, parkID)));
        
    }

    
}
