package com.pocket.naturalist.dto;

import java.util.List;

import org.locationtech.jts.geom.Point;

public record AnimalLocationsDTO(String animal, List<Point> locations) {
    
}
