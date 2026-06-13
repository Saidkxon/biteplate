package com.biteplate.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateOrderItemRequest(
        @NotBlank String menuItemId,
        @Min(1) int quantity,
        String addOns,
        String substitutions,
        String allergenNotes
) {
}
