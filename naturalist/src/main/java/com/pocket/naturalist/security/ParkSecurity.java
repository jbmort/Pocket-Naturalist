package com.pocket.naturalist.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.pocket.naturalist.entity.Enums.Role;
import com.pocket.naturalist.repository.UserRepository;

@Component("parkSecurity")
public class ParkSecurity {
    
    private UserRepository userRepository;

    public ParkSecurity(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public boolean isParkAdmin(Authentication authentication, String parkSlug) {
        String userName = authentication.getName();
        
        // Find the user from DB (to get latest Managed Parks list)
        return userRepository.findByUsername(userName).stream()
                            .filter(user -> user.getRole() == Role.ROLE_ADMIN)
                            .flatMap(user -> user.getManagedParks().stream())
                            .anyMatch(park -> park.getUrlSlug().equals(parkSlug));
    }
}
