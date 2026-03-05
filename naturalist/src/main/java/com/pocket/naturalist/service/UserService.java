package com.pocket.naturalist.service;

import org.springframework.stereotype.Service;

import com.pocket.naturalist.dto.JWTAuthResponse;
import com.pocket.naturalist.dto.RegistrationDTO;
import com.pocket.naturalist.dto.UserDataDto;
import com.pocket.naturalist.entity.User;

@Service
public interface UserService {

    UserDataDto getUserInfo(String userName);

    JWTAuthResponse registerNewUser(RegistrationDTO newUser);
    
    User addCheckinPoints(String username, String parkSlug);
}
