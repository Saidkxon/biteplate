package com.biteplate.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterStaffRequest(
        @NotBlank String staffId,
        @NotBlank String username,
        @NotBlank String password,
        @NotBlank String name,
        @NotBlank String role
) {
}
