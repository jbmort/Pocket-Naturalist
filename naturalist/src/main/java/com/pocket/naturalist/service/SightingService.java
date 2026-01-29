package com.pocket.naturalist.service;


import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import com.pocket.naturalist.dto.SightingMapDTO;


@Service
public interface SightingService {

    boolean createSighting(String animalName, Point locationOfAnimal, Point locationOfReport, String parkSlug);

    SightingMapDTO getSightingsForPark(String parkSlug);
    
}
