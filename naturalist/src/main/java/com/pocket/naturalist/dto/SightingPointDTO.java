package com.pocket.naturalist.dto;

import org.locationtech.jts.geom.Point;

public record SightingPointDTO(Point locationOfAnimal) {
}
