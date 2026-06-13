package com.biteplate.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CustomerReservationRequest(
        @NotBlank String customerName,
        @NotBlank String phone,
        @Min(1) int tableNumber,
        @NotNull @Future LocalDateTime reservationTime
) {
}
