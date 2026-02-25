package com.pocket.naturalist.service;

import org.springframework.stereotype.Service;

import com.pocket.naturalist.dto.UserDataDto;
import com.pocket.naturalist.entity.User;
import com.pocket.naturalist.entity.UserParkStat;
import com.pocket.naturalist.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;


    @Override
    public UserDataDto getUserInfo(String userName) {
        User user = userRepository.findByUsername(userName).orElseThrow();

        int totalPoints = user.getUserParkStats().stream()
                                                .mapToInt(UserParkStat::getLifetimePoints)
                                                .sum();

        return new UserDataDto(userName, totalPoints, user.getBadges());
    }
    
}
