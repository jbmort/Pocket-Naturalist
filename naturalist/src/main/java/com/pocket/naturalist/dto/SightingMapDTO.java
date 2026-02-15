package com.pocket.naturalist.dto;

import java.util.List;

/**
 * @param parkSlug
 * @param animalLocations
 */
public record SightingMapDTO(String parkSlug, List<AnimalLocationsDTO> animalLocations) {
    
}
