package com.pocket.naturalist.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pocket.naturalist.dto.ParkDataDTO;
import com.pocket.naturalist.service.ParkService;

@Controller
@RequestMapping("/park")
public class ParkController {

    ParkService parkService;

    public ParkController(ParkService parkService){
        this.parkService = parkService;
    }

    @GetMapping
    public ResponseEntity<ParkDataDTO> getParkData(String parkSlug){

        ParkDataDTO parkData = parkService.getMainPageParkData(parkSlug);

        return ResponseEntity.ok(parkData);
    }



    
}
