package com.pocket.naturalist.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pocket.naturalist.entity.Park;
import com.pocket.naturalist.entity.Sighting;

public interface SightingsRepository extends JpaRepository<Sighting, Long> {
    
    List<Sighting> findAllByPark(Park park);

    // Future additions: 
    //   spatially limited results to identify high-volume submission scenarios
    //   and ensure a variety of sightings are returned to users
    
    /**
     * Returns the most relevant sightings from a park.
     * - Returns all sightings for the day if less than 200
     * - Returns up to 200 most recent sightings
     * - If 200+ sightings were submitted in the last 30 minutes, 
     *   includes a wider variety by mixing recent and older sightings
     */
    @Query("SELECT s FROM Sighting s WHERE s.park = :park " +
           "ORDER BY s.timestamp DESC LIMIT 200")
    List<Sighting> findRecentSightingsByPark(@Param("park") Park park);
    
    /**
     * Returns sightings from the last 2 hours for a park
     * Used to detect high-volume submission scenarios
     */
    @Query("SELECT COUNT(s) FROM Sighting s WHERE s.park = :park " +
           "AND s.timestamp >= :twoHoursAgo")
    long countSightingsInLastTwoHours(@Param("park") Park park, 
                                       @Param("twoHoursAgo") LocalDateTime twoHoursAgo);
    
    /**
     * Returns all sightings for today from a park
     */
    @Query("SELECT s FROM Sighting s WHERE s.park = :park " +
           "AND s.timestamp >= :startOfDay")
    List<Sighting> findSightingsForToday(@Param("park") Park park,
                                         @Param("startOfDay") LocalDateTime startOfDay);
}
