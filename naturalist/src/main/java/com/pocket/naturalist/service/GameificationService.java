package com.pocket.naturalist.service;

import org.springframework.stereotype.Service;

@Service
public interface GameificationService {

    void addCheckInPointsForUser(String username, String parkSlug);

    void awardFeaturePoints(String username, String parkSlug, long featureId);
    
}
