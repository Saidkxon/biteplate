package com.biteplate.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record BillRequest(
        @NotBlank String pricingType,
        @DecimalMin("0.0") double tipAmount,
        @Min(1) int splitCount
) {
}
