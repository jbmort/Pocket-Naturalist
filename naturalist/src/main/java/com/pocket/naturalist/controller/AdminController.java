package com.pocket.naturalist.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pocket.naturalist.dto.ParkDataDTO;
import com.pocket.naturalist.service.ParkService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ParkService parkService;

    AdminController(ParkService parkService){
        this.parkService = parkService;
    }

    @PreAuthorize("@parkSecurity.isParkAdmin(authentication, #parkSlug)")
    @GetMapping("/park/{parkSlug}/info")
    public ResponseEntity<ParkDataDTO> getAdminParkData(@PathVariable String parkSlug){

        ParkDataDTO data = parkService.getAdminParkData(parkSlug);

        return ResponseEntity.ok(data);
    }
    
}
