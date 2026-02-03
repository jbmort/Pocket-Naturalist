package com.pocket.naturalist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pocket.naturalist.entity.Park;

public interface ParkRepository extends JpaRepository<Park, Long> {
    public Park findByURLSlug(String parkSlug);

    @Query("SELECT EXISTS " +
           "(SELECT 1 FROM Park p JOIN p.boundaries b " +
           "WHERE p.urlSlug = :urlSlug AND ST_Contains(b, :userLocation) )")
    public boolean isPointInParkBoundaries(org.locationtech.jts.geom.Point userLocation, String urlSlug);
    
}
