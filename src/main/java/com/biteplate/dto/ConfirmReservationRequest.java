package com.biteplate.dto;

import jakarta.validation.constraints.NotBlank;

public record ConfirmReservationRequest(
        @NotBlank String staffId
) {
}
