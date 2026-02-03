package com.pocket.naturalist.serviceTests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pocket.naturalist.repository.ParkRepository;
import com.pocket.naturalist.service.LocationServiceImpl;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;


@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    GeometryFactory geometryFactory;;

    @InjectMocks
    LocationServiceImpl locationService;

    @Mock
    ParkRepository parkRepository;

    @BeforeEach
    void setUp() {
        geometryFactory = new GeometryFactory();
    }

    @Test
    void shouldVerifyPointIsNearFeature() {
        Point userLocation = geometryFactory.createPoint(new Coordinate(5, 5));

        when(parkRepository.isPointNearFeature(userLocation, "test-park", 1)).thenReturn(true);
        
        boolean result = locationService.isPointNearFeature(userLocation, "test-park", 1);

        assertTrue(result);
    }

    @Test
    void shouldVerifyPointIsNotNearFeature() {
        Point userLocation = geometryFactory.createPoint(new Coordinate(10, 10));

        when(parkRepository.isPointNearFeature(userLocation, "test-park", 1)).thenReturn(false);

        boolean result = locationService.isPointNearFeature(userLocation, "test-park", 1);
        
        assertFalse(result);
    }
}
