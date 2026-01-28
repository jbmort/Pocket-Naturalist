package com.pocket.naturalist.dto;

import org.locationtech.jts.geom.Point;

import com.pocket.naturalist.entity.Animal;

public record SightingPointDTO(Animal animal, Point locationOfAnimal, long parkId) {
}
