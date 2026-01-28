package com.pocket.naturalist.service;

import java.util.List;

import org.locationtech.jts.geom.Point;

import com.pocket.naturalist.entity.Animal;
import com.pocket.naturalist.entity.Park;
import com.pocket.naturalist.dto.SightingPointDTO;

public class SightingServiceImpl implements SightingService {

    @Override
    public boolean createSighting(Animal animal, Point locationOfAnimal, Point locationOfReport, Park park) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createSighting'");
    }

    @Override
    public List<SightingPointDTO> getSightingsForPark(long parkID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSightingsForPark'");
    }
    
}
