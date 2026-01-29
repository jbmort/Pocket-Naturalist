package com.pocket.naturalist.controller;

import org.apache.catalina.core.ApplicationContext;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pocket.naturalist.dto.AnimalLocationsDTO;
import com.pocket.naturalist.dto.SightingMapDTO;
import com.pocket.naturalist.dto.SightingReportDTO;
import com.pocket.naturalist.entity.Sighting;
import com.pocket.naturalist.service.SightingService;

@RestController
@RequestMapping("/sightings")
public class SightingController {
    private final SightingService sightingService;

    @Autowired
    public SightingController(SightingService sightingService) {
        this.sightingService = sightingService;
    }

    @GetMapping("/{parkSlug}")
    public ResponseEntity<SightingMapDTO> getSightingsForPark(@PathVariable String parkSlug) {
        System.out.println("Received request for sightings in park: " + parkSlug);

        SightingMapDTO sightings = sightingService.getSightingsForPark(parkSlug);

        System.out.println("Returning " + sightings + " sightings for park: " + parkSlug);

        return ResponseEntity.ok(sightings);
    }

    @PostMapping("/{parkSlug}")
    public ResponseEntity<Void> createSighting(@PathVariable String parkSlug,
                                                @RequestBody SightingReportDTO sightingData) 
    {
        System.out.println("Received sighting report for park: " + parkSlug + " with data: " + sightingData);
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