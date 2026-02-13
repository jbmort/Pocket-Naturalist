package com.pocket.naturalist.serviceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pocket.naturalist.dto.ParkDataDTO;
import com.pocket.naturalist.entity.Animal;
import com.pocket.naturalist.entity.Park;
import com.pocket.naturalist.repository.ParkRepository;
import com.pocket.naturalist.service.ParkServiceImpl;

@ExtendWith(MockitoExtension.class)
class ParkServiceTest {

    GeometryFactory geometryFactory;
    Polygon parkBoundary;
    Park park;

    @Mock
    ParkRepository parkRepository;

    @InjectMocks
    ParkServiceImpl parkService;

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
    }

    @Test
    void shouldReturnParkDataForMainPage(){
        ParkDataDTO parkDataDto = new ParkDataDTO(park.getName(), park.getBoundaryList(), park.getFeatures(), park.getAnimals());

        when(parkRepository.findByUrlSlug(park.getUrlSlug())).thenReturn(Optional.of(park));

        ParkDataDTO serviceResult = parkService.getMainPageParkData(park.getUrlSlug());

        assertEquals(parkDataDto, serviceResult);    

    }

    @Test
    void shouldCatchBadParkSlug(){
        when(parkRepository.findByUrlSlug(park.getUrlSlug())).thenReturn(Optional.empty());

        ParkDataDTO serviceResult = parkService.getMainPageParkData(park.getUrlSlug());

        assertEquals(null, serviceResult); 
    }


}
