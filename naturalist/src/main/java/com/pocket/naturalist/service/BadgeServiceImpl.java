package com.pocket.naturalist.service;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pocket.naturalist.entity.Badge;

@Service
public class BadgeServiceImpl implements BadgeService {

    @Value("${badge.url.milestone.one:url}")
    private String milestoneOneURL = "url";
    @Value("${badge.url.milestone.two:url}")
    private String milestoneTwoURL = "url";
    @Value("${badge.url.milestone.three:url}")
    private String milestoneThreeURL = "url";
    @Value("${badge.url.milestone.four:url}")
    private String milestoneFourURL = "url";
    @Value("${badge.url.milestone.five:url}")
    private String milestoneFiveURL = "url";

    private Map<Integer, String> milestoneURLs = Map.of(
        10, milestoneOneURL,
        100, milestoneTwoURL,
        250, milestoneThreeURL,
        500, milestoneFourURL,
        1000, milestoneFiveURL
    );

    /**
     * Creates a milestone badge based on the provided name and milestone.
     * @param parkName the name of the park for which the badge is being created
     * @param milestone the milestone that the badge represents (e.g. 10 visits)
     * @return a new Badge object with the appropriate name and URL
    */
    @Override
    public Badge createMilestoneBadge(String parkName, int milestone) {
        String badgeURL = milestoneURLs.get(milestone);

        String badgeName = parkName + " : " + milestone + " Visits";

        return new Badge(badgeName, badgeURL);
    }

    @Override
    public Set<Integer> getBadgeMilestones() {
     return milestoneURLs.keySet();
    }
    
}
