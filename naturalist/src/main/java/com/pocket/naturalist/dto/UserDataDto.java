package com.pocket.naturalist.dto;

import java.util.List;

import com.pocket.naturalist.entity.Badge;

public record UserDataDto(String userName, int totalPoints, List<Badge> badges) {
    
}
