package com.pocket.naturalist.serviceTests;

import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.pocket.naturalist.entity.Animal;
import com.pocket.naturalist.entity.Park;
import com.pocket.naturalist.entity.Sighting;
import com.pocket.naturalist.repository.SightingsRepository;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.locationtech.jts.geom.Coordinate;

import com.pocket.naturalist.service.SightingService;



public class SightingServiceTest {

    private GeometryFactory geometryFactory = new GeometryFactory();

    @Autowired
    private SightingService sightingService;

    @MockitoBean
    private SightingsRepository sightingsRepository;

    SightingServiceTest(SightingService sightingService) {
        this.sightingService = sightingService;
    }



    @Test
    public void testCreateSighting() {
        Animal animal = new Animal();
        Point locationOfAnimal = geometryFactory.createPoint(new Coordinate(-110.5885, 44.4279));
        Point locationOfReport = geometryFactory.createPoint(new Coordinate(-110.5880, 44.4280));
        Park park = new Park("Yellowstone");

        boolean result = sightingService.createSighting(animal, locationOfAnimal, locationOfReport, park);

        assert(result);
    }

    @Test
    public void shouldReturnListOfSightings() {
        long parkID = 1L;
        Animal animal = new Animal();
        Point locationOfAnimal = geometryFactory.createPoint(new Coordinate(-110.5885, 44.4279));
        Point locationOfReport = geometryFactory.createPoint(new Coordinate(-110.5880, 44.4280));
        Park park = new Park("Yellowstone");

        when(sightingsRepository.getSightingsForPark(parkID))
        .thenReturn(List.of(
            new Sighting(animal, locationOfAnimal, locationOfReport, park)));

        List<Sighting> sightings = sightingService.getSightingsForPark(parkID);

        assert(sightings != null);
        assert(sightings.size() == 1);
        assert(sightings.get(0).getPark().getName().equals("Yellowstone"));
    }

}
    

