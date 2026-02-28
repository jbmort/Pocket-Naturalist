package com.pocket.naturalist.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pocket.naturalist.dto.JWTAuthResponse;
import com.pocket.naturalist.dto.LoginDTO;
import com.pocket.naturalist.security.JwtService;

@RestController
public class LogInController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LogInController(
        AuthenticationManager authenticationManager,
        JwtService jwtService
    ){
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }


    @PostMapping("/login")
    public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody LoginDTO creds){
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        creds.username(),
                        creds.password()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtService.generateToken((UserDetails) authentication.getPrincipal());

            return ResponseEntity.ok(new JWTAuthResponse(token));

    }

    
}
