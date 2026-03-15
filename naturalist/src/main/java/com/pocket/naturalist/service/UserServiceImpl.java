package com.pocket.naturalist.service;

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

    public static final int POINTS_FOR_CHECKIN = 5;

    JwtService jwtService;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    ParkRepository parkRepository;
    GameificationService gameficationService;

    public UserServiceImpl(
            JwtService jwtService,
            UserRepository userRepository,
            ParkRepository parkRepository,
            PasswordEncoder passwordEncoder,
            GameificationService gameificationService) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.parkRepository = parkRepository;
        this.gameficationService = gameificationService;
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

    @Override
    public String addAdminToPark(String parkSlug, String newAdminUsername) {

        User newAdmin = userRepository.findByUsername(newAdminUsername).orElseThrow();
        Park park = parkRepository.findByUrlSlug(parkSlug).orElseThrow();

        if (!newAdmin.getRole().equals(Role.ADMIN)) {
            newAdmin.setRole(Role.ADMIN);
        }
        newAdmin.addManagedPark(park);
        User updatedUser = userRepository.save(newAdmin);

        if (updatedUser.getManagedParks().contains(park)
                && !updatedUser.getRole().equals(Role.ADMIN)
                && updatedUser.getUsername().equals(newAdmin.getUsername())) {

            return "success";
        } else {
            return "failure";
        }
    }

}
