package com.pocket.naturalist.service;

import org.springframework.stereotype.Service;

@Service
public interface GameificationService {

    void addCheckInPointsForUser(long userId, String parkSlug);

    void awardFeaturePoints(long userId, String parkSlug, long featureId);
    
}
