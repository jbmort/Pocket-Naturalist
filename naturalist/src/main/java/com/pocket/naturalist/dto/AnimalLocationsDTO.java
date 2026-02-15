package com.pocket.naturalist.dto;

import java.util.List;

import org.locationtech.jts.geom.Point;

/**
 * @param animal
 * @param locations
 */
public record AnimalLocationsDTO(String animal, List<Point> locations) {
    
}
