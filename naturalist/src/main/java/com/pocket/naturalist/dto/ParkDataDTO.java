package com.pocket.naturalist.dto;

import java.util.List;
import java.util.Set;

import org.locationtech.jts.geom.Polygon;

import com.pocket.naturalist.entity.Animal;
import com.pocket.naturalist.entity.Feature;

public record ParkDataDTO(String parkName, List<Polygon> boundaries, List<Feature> features, Set<Animal> animals) {
    
}
