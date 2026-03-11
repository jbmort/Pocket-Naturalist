package com.pocket.naturalist.service;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.pocket.naturalist.entity.Badge;

@Service
public interface BadgeService {
    
    Badge createMilestoneBadge(String parkName, int milestone);

    Set<Integer> getBadgeMilestones();
}
