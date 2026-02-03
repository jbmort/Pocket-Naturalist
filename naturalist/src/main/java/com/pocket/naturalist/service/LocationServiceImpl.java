package com.pocket.naturalist.service;

import com.pocket.naturalist.repository.ParkRepository;

public class LocationServiceImpl implements LocationService {

    ParkRepository parkRepository;


    @Override
    public boolean isPointInsideParkBoundaries(org.locationtech.jts.geom.Point userLocation, String urlSlug) {

        return parkRepository.isPointInParkBoundaries(userLocation, urlSlug);
    }

    
    
}
