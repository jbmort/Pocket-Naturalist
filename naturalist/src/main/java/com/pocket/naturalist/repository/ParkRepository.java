package com.pocket.naturalist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pocket.naturalist.entity.Park;

public interface ParkRepository extends JpaRepository<Park, Long> {
    public Park findBySlug(String parkSlug);
    
}
