package com.pocket.naturalist.dto;

/**
 * @param parkSlug
 * @param isInsidePark
 */
public record CheckInResponseDTO(String parkSlug, boolean isInsidePark) {
    
}
