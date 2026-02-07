package com.pocket.naturalist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pocket.naturalist.entity.Feature;

public interface FeatureRepository extends JpaRepository<Feature, Long> {
    
}
