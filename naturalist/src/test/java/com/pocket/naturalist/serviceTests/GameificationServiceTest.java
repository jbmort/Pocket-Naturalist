package com.pocket.naturalist.serviceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pocket.naturalist.entity.Feature;
import com.pocket.naturalist.entity.Park;
import com.pocket.naturalist.entity.User;
import com.pocket.naturalist.entity.UserParkStat;
import com.pocket.naturalist.repository.FeatureRepository;
import com.pocket.naturalist.repository.ParkRepository;
import com.pocket.naturalist.repository.UserParkStatRepository;
import com.pocket.naturalist.repository.UserRepository;
import com.pocket.naturalist.service.GameificationServiceImpl;

@ExtendWith(MockitoExtension.class)
class GameificationServiceTest {

    GeometryFactory geometryFactory;

    User testUser;
    Park testPark;

    @Mock
    UserRepository userRepository;

    @Mock
    ParkRepository parkRepository;

    @Mock
    FeatureRepository featureRepository;

    @Mock
    UserParkStatRepository userParkStatRepository;


    @InjectMocks
    GameificationServiceImpl gameificationService;

    // Functions that this service needs to perform are:
    // // Add points for user when checking in
    // // Award badges for accomplishments
    // // Award points for participating or interacting with a feature

    @BeforeEach
    void setup(){
        testUser = new User();
        testUser.setId(11);
        testUser.setUsername("Test User");

        testPark = new Park("test park");
        testPark.setId(2);

        geometryFactory = new GeometryFactory();

    }
    
    @Test
    void shouldAddpointsForUser(){
        long userId = testUser.getId();
        String parkSlug = testPark.getUrlSlug();
        Optional<User> optionalUser = Optional.of(testUser);

        when(userRepository.findById(userId)).thenReturn(optionalUser);
        when(parkRepository.findByUrlSlug(parkSlug)).thenReturn(testPark);
        when(userRepository.save(testUser)).thenReturn(testUser);

        gameificationService.addCheckInPointsForUser(userId, parkSlug);

        List<UserParkStat> parkCheckIns = testUser.getUserParkStats();

        assertNotEquals(0, parkCheckIns.size());
        assertEquals("Test User", parkCheckIns.getFirst().getUser().getUsername());
        assertNotEquals(0, parkCheckIns.getFirst().getLifetimePoints());
        assertNotEquals(0, parkCheckIns.getFirst().getCurrentYearPoints());
    }

    @Test
    void shouldAddPointsForInteractionWithFeature(){
        Point featureLocation = geometryFactory.createPoint(new Coordinate(5, 5));

        Feature feature = new Feature("new Feature", featureLocation, 1, testPark);
        feature.setId(3);
        testPark.addFeature(feature);

        String parkSlug = testPark.getUrlSlug();

        when(parkRepository.findByUrlSlug(parkSlug)).thenReturn(testPark);
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(featureRepository.findById(feature.getId())).thenReturn(Optional.of(feature));


        gameificationService.awardFeaturePoints(testUser.getId(), parkSlug, feature.getId());

        int points = testUser.getParkStat(parkSlug).orElseThrow().getLifetimePoints();
        LocalDateTime lastVisit = testUser.getParkStat(testPark.getUrlSlug()).orElseThrow().getLastVisited();

        // should have points for the check in at the park and for the feature interaction
        assertEquals(2, points);
        assertNotNull(lastVisit);
    }
}
