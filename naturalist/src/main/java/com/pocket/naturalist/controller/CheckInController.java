package com.pocket.naturalist.controller;

import org.locationtech.jts.geom.Point;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pocket.naturalist.dto.CheckInResponseDTO;
import com.pocket.naturalist.dto.CheckInResponseFeatureDTO;
import com.pocket.naturalist.service.LocationService;

@RestController
@RequestMapping("/checkin")
public class CheckInController {

    LocationService locationService;

    public CheckInController(LocationService locationService){
        this.locationService = locationService;
    }


    @PostMapping("/{parkSlug}")
    public ResponseEntity<CheckInResponseDTO> checkIn(@PathVariable String parkSlug,
                                                        @RequestBody Point location)
    {
        boolean isInside = locationService.isPointInsideParkBoundaries(location, parkSlug);

        CheckInResponseDTO responseDTO = new CheckInResponseDTO(parkSlug, isInside);
        return ResponseEntity.ok(responseDTO);
        
    }

    @PostMapping("/{parkSlug}/feature/{featureId}")
    public ResponseEntity<CheckInResponseFeatureDTO> checkInFeature(@PathVariable String parkSlug,
                                                            @PathVariable int featureId,
                                                            @RequestBody Point location){
        
        boolean isNearFeature = locationService.isPointNearFeature(location, parkSlug, featureId);

        CheckInResponseFeatureDTO responseDTO = new CheckInResponseFeatureDTO(parkSlug, featureId, isNearFeature);
        
        return ResponseEntity.ok(responseDTO);
    
    }
}
