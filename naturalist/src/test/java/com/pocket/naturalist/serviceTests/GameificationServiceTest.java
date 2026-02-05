// package com.pocket.naturalist.serviceTests;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.mockito.Mockito.when;

// import java.util.List;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import com.pocket.naturalist.entity.Park;
// import com.pocket.naturalist.entity.User;
// import com.pocket.naturalist.entity.UserParkStat;
// import com.pocket.naturalist.repository.ParkRepository;
// import com.pocket.naturalist.repository.UserRepository;
// import com.pocket.naturalist.service.GameificationService;

// @ExtendWith(MockitoExtension.class)
// class GameificationServiceTest {

//     User testUser;
//     Park testPark;

//     @Mock
//     UserRepository userRepository;

//     @Mock
//     ParkRepository parkRepository;


//     @InjectMocks
//     GameificationService gameificationService;

//     // Functions that this service needs to perform are:
//     // // Add points for user when checking in
//     // // Award badges for accomplishments

//     @BeforeEach
//     void setup(){
//         testUser = new User();
//         testUser.setId(11);
//         testUser.setUsername("Test User");

//         testPark = new Park("test park");
//         testPark.setId(2);
//     }
    
    // @Test
    // void shouldAddpointsForUser(){
    //     long userId = testUser.getId();
    //     String parkSlug = testPark.getUrlSlug();

    //     when(userRepository.findByUsername("Test User")).thenReturn(testUser);
    //     when(parkRepository.findByURLSlug(parkSlug)).thenReturn(testPark);

    //     gameificationService.addCheckInPointsForUser(userId, parkSlug);

    //     List<UserParkStat> parkCheckIns = testUser.getUserParkStats();

    //     assertEquals(1, parkCheckIns.size());
    //     assertEquals("Test User", parkCheckIns.getFirst().getUser().getUsername());
    //     assertEquals(1, parkCheckIns.getFirst().getLifetimePoints());
    //     assertEquals(1, parkCheckIns.getFirst().getCurrentYearPoints());
    // }
// }
