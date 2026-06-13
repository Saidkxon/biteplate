package com.biteplate.dto;

import com.biteplate.domain.order.OrderStatus;

import java.util.List;

public record OrderResponse(
        String orderId,
        int tableNumber,
        String staffId,
        OrderStatus status,
        List<String> items,
        double subtotal
) {
}
