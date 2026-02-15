package com.pocket.naturalist.dto;

/**
 * @param parkSlug
 * @param featureId
 * @param isNear
 */
public record CheckInResponseFeatureDTO(String parkSlug, int featureId, boolean isNear) {
    
}
