package com.pocket.naturalist.repository;

import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pocket.naturalist.entity.Park;

public interface ParkRepository extends JpaRepository<Park, Long> {
    public Park findByURLSlug(String parkSlug);

    @Query("SELECT EXISTS " +
           "(SELECT 1 FROM Park p JOIN p.boundaries b " +
           "WHERE p.URLSlug = :urlSlug AND ST_Contains(b, :userLocation) )")
    public boolean isPointInParkBoundaries(org.locationtech.jts.geom.Point userLocation, String urlSlug);

    @Query("SELECT EXISTS " +
           "(SELECT 1 FROM Park p JOIN p.features f " +
           "WHERE p.URLSlug = :urlSlug AND f.id = :featureId AND ST_DWithin(f.location, :userLocation, 0.001) )")
    public boolean isPointNearFeature(Point userLocation, String urlSlug, int featureId);
    
}
