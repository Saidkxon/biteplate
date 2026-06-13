package com.biteplate.domain.order;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer_orders")
public class CustomerOrder {
    @Id
    private String orderId;

    @Column(nullable = false)
    private int tableNumber;

    @Column(nullable = false)
    private String staffId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private List<OrderItem> items = new ArrayList<>();

    protected CustomerOrder() {
    }

    public CustomerOrder(String orderId, int tableNumber, String staffId) {
        if (orderId == null || orderId.isBlank()) throw new IllegalArgumentException("Order ID is required.");
        if (tableNumber <= 0) throw new IllegalArgumentException("Table number must be positive.");
        if (staffId == null || staffId.isBlank()) throw new IllegalArgumentException("Staff ID is required.");
        this.orderId = orderId;
        this.tableNumber = tableNumber;
        this.staffId = staffId;
        this.status = OrderStatus.CREATED;
        this.createdAt = LocalDateTime.now();
    }

    public String getOrderId() { return orderId; }
    public int getTableNumber() { return tableNumber; }
    public String getStaffId() { return staffId; }
    public OrderStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<OrderItem> getItems() { return items; }

    public void setStatus(OrderStatus status) {
        if (status == null) throw new IllegalArgumentException("Status cannot be null.");
        this.status = status;
    }

    public void addItem(OrderItem item) {
        if (status != OrderStatus.CREATED && status != OrderStatus.SENT_TO_KITCHEN) {
            throw new IllegalStateException("Order can only be modified before preparation begins.");
        }
        items.add(item);
    }

    public void removeLastItem() {
        if (status != OrderStatus.CREATED && status != OrderStatus.SENT_TO_KITCHEN) {
            throw new IllegalStateException("Order can only be modified before preparation begins.");
        }
        if (!items.isEmpty()) items.remove(items.size() - 1);
    }

    public boolean hasAllergenAlert() {
        return items.stream().anyMatch(OrderItem::hasAllergenAlert);
    }

    public double getSubtotal() {
        return items.stream().mapToDouble(OrderItem::getLineTotal).sum();
    }
}
