package com.pocket.naturalist.service;

import org.springframework.stereotype.Service;

import com.pocket.naturalist.dto.ParkDataDTO;

@Service
public interface ParkService {

    ParkDataDTO getMainPageParkData(String parkSlug);

    Object getAdminParkData(String parkSlug);
    
}
