package com.pocket.naturalist.serviceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.pocket.naturalist.dto.SightingMapDTO;
import com.pocket.naturalist.entity.Animal;
import com.pocket.naturalist.entity.Park;
import com.pocket.naturalist.entity.Sighting;
import com.pocket.naturalist.repository.AnimalRepository;
import com.pocket.naturalist.repository.ParkRepository;
import com.pocket.naturalist.repository.SightingsRepository;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.locationtech.jts.geom.Coordinate;

import com.pocket.naturalist.service.SightingServiceImpl;

@ExtendWith(MockitoExtension.class)
class SightingServiceTest {

    private GeometryFactory geometryFactory = new GeometryFactory();

    @InjectMocks
    private SightingServiceImpl sightingService;

    @Mock
    private SightingsRepository sightingsRepository;

    @Mock
    private ParkRepository parkRepository;

    @Mock
    private AnimalRepository animalRepository;

   

    @Test
    void testCreateSighting() {
        when(animalRepository.findByCommonName("animal")).thenReturn(new Animal("animal", "Animal", "An animal")) ;
        when(parkRepository.findByURLSlug("yellowstone"))
        .thenReturn(new Park("Yellowstone"));

        Point locationOfAnimal = geometryFactory.createPoint(new Coordinate(-110.5885, 44.4279));
        Point locationOfReport = geometryFactory.createPoint(new Coordinate(-110.5880, 44.4280));

        boolean result = sightingService.createSighting("animal", locationOfAnimal, locationOfReport, "yellowstone");

        assertTrue(result);
    }

    @Test
    void shouldReturnListOfSightings() {
        Animal animal = new Animal("Canis lupus", "Gray Wolf", "A large canine native to North America and Eurasia.");
        Point locationOfAnimal = geometryFactory.createPoint(new Coordinate(-110.5885, 44.4279));
        Point locationOfReport = geometryFactory.createPoint(new Coordinate(-110.5880, 44.4280));
        Park park = new Park("Yellowstone");
        park.setAnimals(Set.of(animal));

        when(sightingsRepository.findAllByPark(park))
        .thenReturn(List.of(
            new Sighting(animal, locationOfAnimal, locationOfReport, park)));

        when(parkRepository.findByURLSlug("yellowstone"))
        .thenReturn(park);

        SightingMapDTO sightings = sightingService.getSightingsForPark(park.getURLSlug());

        assertNotNull(sightings);
        assertEquals(1, sightings.animalLocations().size());
        assertEquals(1, sightings.animalLocations().get(0).locations().size());
        assertEquals("yellowstone", sightings.park());
    }

}
    

