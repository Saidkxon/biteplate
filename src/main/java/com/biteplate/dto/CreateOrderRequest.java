package com.biteplate.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateOrderRequest(
        @Min(1) int tableNumber,
        @NotBlank String staffId,
        @NotEmpty List<@Valid CreateOrderItemRequest> items
) {
}
