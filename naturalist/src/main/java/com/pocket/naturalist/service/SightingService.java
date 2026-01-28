package com.pocket.naturalist.service;

import java.util.List;

import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import com.pocket.naturalist.dto.SightingPointDTO;
import com.pocket.naturalist.entity.Animal;
import com.pocket.naturalist.entity.Park;

@Service
public interface SightingService {

    boolean createSighting(Animal animal, Point locationOfAnimal, Point locationOfReport, Park park);

    List<SightingPointDTO> getSightingsForPark(long parkID);
    
}
