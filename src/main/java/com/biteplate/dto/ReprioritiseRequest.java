package com.biteplate.dto;

import jakarta.validation.constraints.NotBlank;

public record ReprioritiseRequest(
        @NotBlank String orderId
) {
}
