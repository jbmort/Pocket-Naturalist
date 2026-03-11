package com.pocket.naturalist.serviceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.pocket.naturalist.entity.Badge;
import com.pocket.naturalist.service.BadgeServiceImpl;


class BadgeServiceTest {


    @MockitoBean
    BadgeServiceImpl badgeService;

    @BeforeEach
    void setUp() {
        badgeService = new BadgeServiceImpl();
    }


    @Test
    void shouldReturnBadgeMilestones(){

        Set<Integer> milestones = badgeService.getBadgeMilestones();

        assertTrue(milestones.size() > 0);

    }

    @Test
    void shouldCreateBadge(){

        int milestone = 10;

        Badge createdBadge =badgeService.createMilestoneBadge("Test Park", milestone);

        assertEquals("url", createdBadge.getUrl());
        assertTrue(createdBadge.getName().startsWith("Test Park"));


    }
    
}
