package com.biteplate.domain.history;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_history_records")
public class OrderHistoryRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId;
    private int tableNumber;
    private String staffId;

    @Column(length = 2000)
    private String itemSummary;

    private double total;
    private LocalDateTime timestamp;

    protected OrderHistoryRecord() {
    }

    public OrderHistoryRecord(String orderId, int tableNumber, String staffId, String itemSummary, double total, LocalDateTime timestamp) {
        this.orderId = orderId;
        this.tableNumber = tableNumber;
        this.staffId = staffId;
        this.itemSummary = itemSummary;
        this.total = total;
        this.timestamp = timestamp;
    }

    public Long getId() { return id; }
    public String getOrderId() { return orderId; }
    public int getTableNumber() { return tableNumber; }
    public String getStaffId() { return staffId; }
    public String getItemSummary() { return itemSummary; }
    public double getTotal() { return total; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
