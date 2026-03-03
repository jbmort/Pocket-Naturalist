package com.pocket.naturalist.service;

import org.springframework.stereotype.Service;

import com.pocket.naturalist.dto.JWTAuthResponse;
import com.pocket.naturalist.dto.RegistrationDTO;
import com.pocket.naturalist.dto.UserDataDto;
import com.pocket.naturalist.entity.User;
import com.pocket.naturalist.entity.UserParkStat;
import com.pocket.naturalist.entity.Enums.Role;
import com.pocket.naturalist.repository.UserRepository;
import com.pocket.naturalist.security.JwtService;
import com.pocket.naturalist.security.SecurityUser;

@Service
public class UserServiceImpl implements UserService {


    private final JwtService jwtService = new JwtService();

    UserRepository userRepository;


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

        User user = new User();
        user.setUsername(username);
        user.setRole(Role.USER);
        user.setPassword(password);

        userRepository.save(user);

        String token = jwtService.generateToken(new SecurityUser(user));

        return new JWTAuthResponse(token);

    }
    
}
