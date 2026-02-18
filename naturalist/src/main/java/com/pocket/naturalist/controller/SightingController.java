package com.pocket.naturalist.controller;

import org.locationtech.jts.geom.Point;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pocket.naturalist.dto.SightingMapDTO;
import com.pocket.naturalist.dto.SightingReportDTO;
import com.pocket.naturalist.service.SightingService;

@RestController
@RequestMapping("/sightings")
public class SightingController {
    private final SightingService sightingService;

    public SightingController(SightingService sightingService) {
        this.sightingService = sightingService;
    }

    @GetMapping("/{parkSlug}")
    public ResponseEntity<SightingMapDTO> getSightingsForPark(@PathVariable String parkSlug
    ) {

        SightingMapDTO sightings = sightingService.getSightingsForPark(parkSlug);

        return ResponseEntity.ok(sightings);
    }

    @PostMapping("/{parkSlug}")
    public ResponseEntity<Void> createSighting(@PathVariable String parkSlug,
                                                @RequestBody SightingReportDTO sightingData) 
    {
        String animalName = sightingData.animalName();
        Point locationOfAnimal = sightingData.locationOfAnimal();
        Point locationOfReport = sightingData.locationOfReport();
        boolean created = sightingService.createSighting(animalName, locationOfAnimal, locationOfReport, parkSlug);

        if(!created) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
}