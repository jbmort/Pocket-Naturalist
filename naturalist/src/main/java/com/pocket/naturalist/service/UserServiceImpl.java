package com.pocket.naturalist.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pocket.naturalist.dto.JWTAuthResponse;
import com.pocket.naturalist.dto.RegistrationDTO;
import com.pocket.naturalist.dto.UserDataDto;
import com.pocket.naturalist.entity.Park;
import com.pocket.naturalist.entity.User;
import com.pocket.naturalist.entity.UserParkStat;
import com.pocket.naturalist.entity.Enums.Role;
import com.pocket.naturalist.repository.ParkRepository;
import com.pocket.naturalist.repository.UserRepository;
import com.pocket.naturalist.security.JwtService;
import com.pocket.naturalist.security.SecurityUser;

@Service
public class UserServiceImpl implements UserService {

    int POINTS_FOR_CHECKIN = 5;

    JwtService jwtService;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    ParkRepository parkRepository;

    public UserServiceImpl(
        JwtService jwtService,
        UserRepository userRepository,
        ParkRepository parkRepository,
        PasswordEncoder passwordEncoder
    ){
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.parkRepository = parkRepository;
    }


    @Override
    public UserDataDto getUserInfo(String userName) {
        User user = userRepository.findByUsername(userName).orElseThrow();

        int totalPoints = user.getUserParkStats().stream()
                                                .mapToInt(UserParkStat::getLifetimePoints)
                                                .sum();

        return new UserDataDto(userName, totalPoints, user.getBadges());
    }


    @Override
    public JWTAuthResponse registerNewUser(RegistrationDTO newUser) {
        String username = newUser.username();
        String password = newUser.password();

        if (userRepository.existsByUsername(username)) {
            throw new IllegalStateException("Error: Username is already taken!");
        }

        User user = new User();
        user.setUsername(username);
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);

        String token = jwtService.generateToken(new SecurityUser(user));

        return new JWTAuthResponse(token);
    }


    public User addCheckinPoints(String username, String parkSlug) {
        User user = userRepository.findByUsername(username).orElseThrow();

        UserParkStat stat = user.getParkStat(parkSlug).orElseThrow();

        if(!stat.getLastVisited().isAfter(LocalDateTime.now().minusDays(1))){
            stat.addPoints(POINTS_FOR_CHECKIN);
            userRepository.save(user);
            return user;
        }

        return user;
     
    }
    
}
