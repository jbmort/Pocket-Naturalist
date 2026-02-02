package com.pocket.naturalist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pocket.naturalist.entity.Park;
import com.pocket.naturalist.entity.Sighting;

public interface SightingsRepository extends JpaRepository<Sighting, Long>{
    List<Sighting> findAllByPark(Park park);
    
}
