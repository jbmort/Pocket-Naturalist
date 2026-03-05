package com.pocket.naturalist.serviceTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.pocket.naturalist.dto.JWTAuthResponse;
import com.pocket.naturalist.dto.RegistrationDTO;
import com.pocket.naturalist.dto.UserDataDto;
import com.pocket.naturalist.entity.Badge;
import com.pocket.naturalist.entity.Park;
import com.pocket.naturalist.entity.User;
import com.pocket.naturalist.entity.UserParkStat;
import com.pocket.naturalist.entity.Enums.Role;
import com.pocket.naturalist.repository.UserRepository;
import com.pocket.naturalist.security.JwtService;
import com.pocket.naturalist.security.SecurityUser;
import com.pocket.naturalist.service.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldReturnUserInfoWithCalculatedPoints() {
        String username = "testUser";
        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setBadges(List.of(new Badge("Explorer", "icon.png")));

        Park park1 = new Park("park1");
        Park park2 = new Park("park2");

        UserParkStat stat1 = new UserParkStat(mockUser, park1);
        stat1.setLifetimePoints(10);
        stat1.setLastVisited(LocalDateTime.now().minusDays(2));
        UserParkStat stat2 = new UserParkStat(mockUser, park2);
        stat2.setLifetimePoints(25);
        stat2.setLastVisited(LocalDateTime.now());
        mockUser.setUserParkStats(List.of(stat1, stat2));

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        UserDataDto result = userService.getUserInfo(username);

        assertNotNull(result);
        assertEquals(username, result.userName());
        assertEquals(35, result.totalPoints());
        assertEquals(1, result.badges().size());
        verify(userRepository).findByUsername(username);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        String username = "ghost";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.getUserInfo(username));
    }

    @Test
    void shouldReturnZeroPointsIfNoStats() {
        String username = "newbie";
        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setUserParkStats(Collections.emptyList());

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        UserDataDto result = userService.getUserInfo(username);

        assertEquals(0, result.totalPoints());
    }


    @Test
    void shouldRegisterNewUserSuccessfully() {
        RegistrationDTO newUserDto = new RegistrationDTO("email@test.com", "securePass", "newUser");
        String encodedPass = "encoded_securePass";
        String expectedToken = "mock_jwt_token";

        when(userRepository.existsByUsername(newUserDto.username())).thenReturn(false);
        when(passwordEncoder.encode(newUserDto.password())).thenReturn(encodedPass);
        
        when(jwtService.generateToken(any(SecurityUser.class))).thenReturn(expectedToken);

        JWTAuthResponse response = userService.registerNewUser(newUserDto);

        assertNotNull(response);
        assertEquals(expectedToken, response.getAccessToken());

        verify(userRepository).save(argThat(user -> 
            user.getUsername().equals("email@test.com") &&
            user.getPassword().equals(encodedPass) &&
            user.getRole() == Role.USER
        ));
    }

    @Test
    void shouldThrowExceptionIfUsernameTaken() {
        RegistrationDTO newUserDto = new RegistrationDTO("takenUser", "pass", "email");
        when(userRepository.existsByUsername(newUserDto.username())).thenReturn(true);

        IllegalStateException exception = assertThrows(IllegalStateException.class, 
            () -> userService.registerNewUser(newUserDto));
        
        assertEquals("Error: Username is already taken!", exception.getMessage());

        // Verify we NEVER saved the user or generated a token
        verify(userRepository, never()).save(any());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void shouldAddPointsForFirstDailyCheckin(){
        String username = "testUser";
        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setBadges(List.of(new Badge("Explorer", "icon.png")));

        Park park1 = new Park("park1");
        Park park2 = new Park("park2");

        UserParkStat stat1 = new UserParkStat(mockUser, park1);
        stat1.setLifetimePoints(10);
        stat1.setLastVisited(LocalDateTime.now().minusDays(2));
        UserParkStat stat2 = new UserParkStat(mockUser, park2);
        stat2.setLifetimePoints(25);
        stat2.setLastVisited(LocalDateTime.now());
        mockUser.setUserParkStats(List.of(stat1, stat2));

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        User result = userService.addCheckinPoints(username, "park1");

        int points = result.getParkStat("park1").get().getLifetimePoints();
        int expectedPoints = 15;

        assertEquals(expectedPoints, points);
    }

    @Test
    void shouldNotAddPointsIfAlreadyVisited(){
        String username = "testUser";
        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setBadges(List.of(new Badge("Explorer", "icon.png")));

        Park park1 = new Park("park1");
        Park park2 = new Park("park2");

        UserParkStat stat1 = new UserParkStat(mockUser, park1);
        stat1.setLifetimePoints(10);
        stat1.setLastVisited(LocalDateTime.now().minusDays(2));
        UserParkStat stat2 = new UserParkStat(mockUser, park2);
        stat2.setLifetimePoints(25);
        stat2.setLastVisited(LocalDateTime.now());
        mockUser.setUserParkStats(List.of(stat1, stat2));

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        User result = userService.addCheckinPoints(username, "park2");

        int points = result.getParkStat("park2").get().getLifetimePoints();
        int expectedPoints = 25;

        assertEquals(expectedPoints, points);
    }

}