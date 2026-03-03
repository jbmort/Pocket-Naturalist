package com.pocket.naturalist.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pocket.naturalist.dto.JWTAuthResponse;
import com.pocket.naturalist.dto.LoginDTO;
import com.pocket.naturalist.dto.RegistrationDTO;
import com.pocket.naturalist.security.JwtService;
import com.pocket.naturalist.service.UserService;

@RestController
@RequestMapping("/auth")
public class LogInController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    public LogInController(
        AuthenticationManager authenticationManager,
        JwtService jwtService,
        UserService userService
    ){
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }


    @PostMapping("/login")
    public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody LoginDTO creds){
        JWTAuthResponse jwt = createUserJWT(creds.username(), creds.password());

        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/register")
    public ResponseEntity<JWTAuthResponse> registerUser(@RequestBody RegistrationDTO newUser){

        JWTAuthResponse jwt = userService.registerNewUser(newUser);
    
        return ResponseEntity.ok(jwt);
    }

    private JWTAuthResponse createUserJWT(String userName, String password){
         Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userName,
                        password
                )
            );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtService.generateToken((UserDetails) authentication.getPrincipal());

        return new JWTAuthResponse(token);
    }

    
}
