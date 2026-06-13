package com.biteplate.domain.table;

import jakarta.persistence.*;

@Entity
@Table(name = "restaurant_tables")
public class RestaurantTable {
    @Id
    private Integer tableNumber;

    @Column(nullable = false)
    private int capacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TableStatus status;

    protected RestaurantTable() {
    }

    public RestaurantTable(int tableNumber, int capacity) {
        if (tableNumber <= 0) throw new IllegalArgumentException("Table number must be positive.");
        if (capacity <= 0) throw new IllegalArgumentException("Capacity must be positive.");
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.status = TableStatus.FREE;
    }

    public Integer getTableNumber() { return tableNumber; }
    public int getCapacity() { return capacity; }
    public TableStatus getStatus() { return status; }

    public void reserve() {
        if (status != TableStatus.FREE && status != TableStatus.CLEARED) {
            throw new IllegalStateException("Only free or cleared tables can be reserved.");
        }
        status = TableStatus.RESERVED;
    }

    public void seatCustomer() {
        if (status != TableStatus.FREE && status != TableStatus.RESERVED && status != TableStatus.CLEARED) {
            throw new IllegalStateException("Table is not available.");
        }
        status = TableStatus.OCCUPIED;
    }

    public void markAwaitingBill() { status = TableStatus.AWAITING_BILL; }
    public void clearTable() { status = TableStatus.CLEARED; }
}
