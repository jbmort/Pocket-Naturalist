package com.pocket.naturalist.controller;

import org.locationtech.jts.geom.Point;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pocket.naturalist.dto.CheckInResponseDTO;
import com.pocket.naturalist.dto.CheckInResponseFeatureDTO;
import com.pocket.naturalist.service.GameificationService;
import com.pocket.naturalist.service.LocationService;
import com.pocket.naturalist.service.UserService;

@RestController
@RequestMapping("/checkin")
public class CheckInController {

    LocationService locationService;
    UserService userService;
    GameificationService gameificationService;

    public CheckInController(LocationService locationService, UserService userService, GameificationService gameificationService){
        this.locationService = locationService;
        this.userService = userService;
        this.gameificationService = gameificationService;
    }


    @PostMapping("/{parkSlug}")
    public ResponseEntity<CheckInResponseDTO> checkIn(@PathVariable String parkSlug,
                                                        @RequestBody Point location,
                                                    Authentication authentication)
    {
        boolean isInside = locationService.isPointInsideParkBoundaries(location, parkSlug);
        CheckInResponseDTO responseDTO = new CheckInResponseDTO(parkSlug, isInside);

        if(isInside){
            String username = authentication.getName();
            gameificationService.addCheckInPointsForUser(username, parkSlug);
        }

        return ResponseEntity.ok(responseDTO);
        
    }

    @PostMapping("/{parkSlug}/feature/{featureId}")
    public ResponseEntity<CheckInResponseFeatureDTO> checkInFeature(@PathVariable String parkSlug,
                                                            @PathVariable long featureId,
                                                            @RequestBody Point location,
                                                            Authentication authentication){
        
        boolean isNearFeature = locationService.isPointNearFeature(location, parkSlug, featureId);

        CheckInResponseFeatureDTO responseDTO = new CheckInResponseFeatureDTO(parkSlug, featureId, isNearFeature);

        if(isNearFeature){
            String username = authentication.getName();
            gameificationService.awardPointsForFeatureCheckIn(username, featureId, parkSlug);
        }
        
        return ResponseEntity.ok(responseDTO);
    
    }
}
