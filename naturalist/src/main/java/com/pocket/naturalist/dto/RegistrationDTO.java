package com.pocket.naturalist.dto;

import jakarta.annotation.Nullable;

public record RegistrationDTO(String username, String password, @Nullable String name) {
    
}
