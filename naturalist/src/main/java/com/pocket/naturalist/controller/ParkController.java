package com.pocket.naturalist.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pocket.naturalist.dto.ParkDataDTO;
import com.pocket.naturalist.service.ParkService;

@RestController
@RequestMapping("/park")
public class ParkController {

    ParkService parkService;

    public ParkController(ParkService parkService){
        this.parkService = parkService;
    }

    @GetMapping("/{parkSlug}")
    public ResponseEntity<ParkDataDTO> getParkData(@PathVariable String parkSlug){

        ParkDataDTO parkData = parkService.getMainPageParkData(parkSlug);
        if(parkData != null){
                    return ResponseEntity.ok(parkData);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }



    
}
