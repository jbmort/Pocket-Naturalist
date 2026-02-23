package com.pocket.naturalist.service;

import org.springframework.stereotype.Service;

import com.pocket.naturalist.dto.UserDataDto;

@Service
public interface UserService {

    UserDataDto getUserInfo(String userName);
    
}
