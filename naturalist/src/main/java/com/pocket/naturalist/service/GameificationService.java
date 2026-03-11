package com.pocket.naturalist.service;

import org.springframework.stereotype.Service;

import com.pocket.naturalist.entity.Park;
import com.pocket.naturalist.entity.User;

@Service
public interface GameificationService {

    void addCheckInPointsForUser(String username, String parkSlug);

    void awardFeaturePoints(String username, String parkSlug, long featureId);

    void checkForMilestoneBadgeAward(User user, Park park);
    
}
