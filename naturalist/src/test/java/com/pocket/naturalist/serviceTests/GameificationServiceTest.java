package com.pocket.naturalist.serviceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.pocket.naturalist.entity.Park;
import com.pocket.naturalist.entity.User;
import com.pocket.naturalist.entity.UserParkStat;
import com.pocket.naturalist.repository.ParkRepository;
import com.pocket.naturalist.repository.UserRepository;
import com.pocket.naturalist.service.GameificationServiceImpl;

@ExtendWith(MockitoExtension.class)
class GameificationServiceTest {

    User testUser;
    Park testPark;

    @Mock
    UserRepository userRepository;

    @Mock
    ParkRepository parkRepository;


    @InjectMocks
    GameificationServiceImpl gameificationService;

    // Functions that this service needs to perform are:
    // // Add points for user when checking in
    // // Award badges for accomplishments

    @BeforeEach
    void setup(){
        testUser = new User();
        testUser.setId(11);
        testUser.setUsername("Test User");

        testPark = new Park("test park");
        testPark.setId(2);
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

        assertEquals(1, parkCheckIns.size());
        assertEquals("Test User", parkCheckIns.getFirst().getUser().getUsername());
        assertEquals(1, parkCheckIns.getFirst().getLifetimePoints());
        assertEquals(1, parkCheckIns.getFirst().getCurrentYearPoints());
    }
}
