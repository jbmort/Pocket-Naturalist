package com.pocket.naturalist.controller;

import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pocket.naturalist.dto.CheckInResponseDTO;
import com.pocket.naturalist.service.LocationService;

@RestController
@RequestMapping("/checkin")
public class CheckInController {

    @Autowired
    LocationService locationService;


    @PostMapping("/{parkSlug}")
    public ResponseEntity<CheckInResponseDTO> checkIn(@PathVariable String parkSlug,
                                                        @RequestBody Point location)
    {
        boolean isInside = locationService.isPointInsideParkBoundaries(location, parkSlug);

        CheckInResponseDTO responseDTO = new CheckInResponseDTO(parkSlug, isInside);
        return ResponseEntity.ok(responseDTO);
        
    }
}
