package com.pocket.naturalist.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pocket.naturalist.dto.UserDataDto;
import com.pocket.naturalist.service.UserService;

@RestController
@RequestMapping("/user")
public class UserAccountController {

    UserService userService;

    public UserAccountController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/data")
    public ResponseEntity<UserDataDto> updateUserInfo(Authentication auth){
        String userName = auth.getName();

        UserDataDto data = userService.getUserInfo(userName);
        return ResponseEntity.ok(data);
    }
    
}
