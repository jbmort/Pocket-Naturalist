package com.pocket.naturalist.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pocket.naturalist.dto.ParkDataDTO;
import com.pocket.naturalist.service.ParkService;
import com.pocket.naturalist.service.UserService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ParkService parkService;
    private final UserService userService;

    AdminController(ParkService parkService, UserService userService){
        this.parkService = parkService;
        this.userService = userService;
    }

    @PreAuthorize("@parkSecurity.isParkAdmin(authentication, #parkSlug)")
    @GetMapping("/park/{parkSlug}")
    public ResponseEntity<ParkDataDTO> getAdminParkData(@PathVariable String parkSlug){

        ParkDataDTO data = parkService.getAdminParkData(parkSlug);

        return ResponseEntity.ok(data);
    }

    @PreAuthorize("@parkSecurity.isParkAdmin(authentication, #parkSlug)")
    @PutMapping("/park/{parkSlug}")
    public ResponseEntity<ParkDataDTO> updateParkData(@PathVariable String parkSlug,
                                                        @RequestBody ParkDataDTO data){
        ParkDataDTO updatedData = parkService.updateParkData(parkSlug, data);
        return ResponseEntity.ok(updatedData);
    }

    @PreAuthorize("@parkSecurity.isParkAdmin(authentication, #parkSlug)")
    @PostMapping("/park/{parkSlug}/admins")
    public ResponseEntity<String> addAdminToPark(@PathVariable String parkSlug, @RequestBody String newAdminUsername){
        String result = userService.addAdminToPark(parkSlug, newAdminUsername);
        return ResponseEntity.status(result.equals("success") ? 201 : 400).body(result);
    }

}