package com.pocket.naturalist.service;

import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import com.pocket.naturalist.repository.ParkRepository;

@Service
public class LocationServiceImpl implements LocationService {

    ParkRepository parkRepository;


    @Override
    public boolean isPointInsideParkBoundaries(org.locationtech.jts.geom.Point userLocation, String urlSlug) {

        return parkRepository.isPointInParkBoundaries(userLocation, urlSlug);
    }


    @Override
    public boolean isPointNearFeature(Point userLocation, String urlSlug, int featureId) {
        return parkRepository.isPointNearFeature(userLocation, urlSlug, featureId);
    }

    
    
}
