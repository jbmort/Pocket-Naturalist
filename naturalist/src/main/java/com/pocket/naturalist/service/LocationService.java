package com.pocket.naturalist.service;

import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

@Service
public interface LocationService {

    boolean isPointInsideParkBoundaries(Point userLocation, String urlSlug);

    boolean isPointNearFeature(Point userLocation, String urlSlug, int featureId);
    
}
