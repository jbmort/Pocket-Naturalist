package com.pocket.naturalist.service;


import org.locationtech.jts.geom.Point;

import com.pocket.naturalist.entity.Animal;
import com.pocket.naturalist.entity.Park;
import com.pocket.naturalist.dto.SightingMapDTO;

public class SightingServiceImpl implements SightingService {

    @Override
    public boolean createSighting(String animalName, Point locationOfAnimal, Point locationOfReport, String parkSlug) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createSighting'");
    }

    @Override
    public SightingMapDTO getSightingsForPark(String parkSlug) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSightingsForPark'");
    }
    
}
