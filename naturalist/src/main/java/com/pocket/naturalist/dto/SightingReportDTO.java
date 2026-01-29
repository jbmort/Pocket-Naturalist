package com.pocket.naturalist.dto;

import org.locationtech.jts.geom.Point;

public record SightingReportDTO(String animalName, Point locationOfAnimal, Point locationOfReport) {
    
}
