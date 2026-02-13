package com.pocket.naturalist.repository;

import java.util.Optional;

import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pocket.naturalist.entity.Park;

public interface ParkRepository extends JpaRepository<Park, Long> {
    public Optional<Park> findByUrlSlug(String parkSlug);

    @Query(value =  """
            SELECT EXISTS (
            SELECT 1
            FROM parks p
            WHERE p.urlslug = :urlSlug AND ST_Contains(p.boundary_list, :userLocation) = true)""", 
            nativeQuery = true)    
    public boolean isPointInParkBoundaries(org.locationtech.jts.geom.Point userLocation, String urlSlug);

 
    @Query(value = """
            SELECT EXISTS (
            SELECT 1 
            FROM parks p, features f 
            WHERE p.urlslug = :urlSlug 
            AND f.id = :featureId 
            AND ST_DWithin(f.location, :userLocation, 0.001)
        )
        """, nativeQuery = true)
    public boolean isPointNearFeature(Point userLocation, String urlSlug, int featureId);
    
}
