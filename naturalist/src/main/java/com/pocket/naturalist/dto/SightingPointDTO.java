package com.pocket.naturalist.dto;

import org.locationtech.jts.geom.Point;

/**
 * @param locationOfAnimal
 */
public record SightingPointDTO(Point locationOfAnimal) {
}
