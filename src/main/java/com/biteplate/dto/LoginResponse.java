package com.biteplate.dto;

public record LoginResponse(
        String staffId,
        String name,
        String role,
        String message
) {
}
