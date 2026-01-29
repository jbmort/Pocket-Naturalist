package com.pocket.naturalist.dto;

import java.util.List;

public record SightingMapDTO(String park, List<AnimalLocationsDTO> animalLocations) {
    
}
