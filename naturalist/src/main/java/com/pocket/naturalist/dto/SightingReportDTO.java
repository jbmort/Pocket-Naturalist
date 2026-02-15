package com.pocket.naturalist.dto;

import org.locationtech.jts.geom.Point;

/**
 * @param animalName
 * @param locationOfAnimal
 * @param locationOfReport
 */
public record SightingReportDTO(String animalName, Point locationOfAnimal, Point locationOfReport) {
    
}
