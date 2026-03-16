package com.pocket.naturalist.dto;

import java.time.LocalDateTime;

public record ApiErrorResponse(
    int status,
    String error,
    String message,
    LocalDateTime timestamp) {
    
        public ApiErrorResponse(int status, String error, String message) {
        this(status, error, message, LocalDateTime.now());
    }
}
