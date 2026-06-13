package com.biteplate.dto;

public record BillResponse(
        String orderId,
        int tableNumber,
        String pricingStrategy,
        double subtotal,
        double discountedTotal,
        double taxAmount,
        double tipAmount,
        int splitCount,
        double splitAmount,
        double finalTotal
) {
}
