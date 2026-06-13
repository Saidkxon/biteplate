package com.biteplate.controller;

import com.biteplate.domain.order.CustomerOrder;
import com.biteplate.domain.order.OrderItem;
import com.biteplate.dto.*;
import com.biteplate.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    public OrderController(OrderService orderService) { this.orderService = orderService; }

    @GetMapping
    public List<OrderResponse> all() { return orderService.allOrders().stream().map(this::map).toList(); }

    @GetMapping("/{orderId}")
    public OrderResponse one(@PathVariable String orderId) { return map(orderService.get(orderId)); }

    @PostMapping
    public OrderResponse create(@Valid @RequestBody CreateOrderRequest request) { return map(orderService.create(request)); }

    @PostMapping("/{orderId}/items")
    public OrderResponse addItem(@PathVariable String orderId, @Valid @RequestBody AddOrderItemRequest request) {
        return map(orderService.addItem(orderId, request));
    }

    @DeleteMapping("/{orderId}/items/last")
    public OrderResponse removeLast(@PathVariable String orderId) {
        return map(orderService.removeLastItem(orderId));
    }

    private OrderResponse map(CustomerOrder order) {
        return new OrderResponse(
                order.getOrderId(),
                order.getTableNumber(),
                order.getStaffId(),
                order.getStatus(),
                order.getItems().stream().map(OrderItem::display).toList(),
                order.getSubtotal()
        );
    }
}
