package com.biteplate.domain.billing;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bill_records")
public class BillRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId;
    private int tableNumber;
    private String pricingStrategy;
    private double subtotal;
    private double discountedTotal;
    private double taxAmount;
    private double tipAmount;
    private int splitCount;
    private double splitAmount;
    private double finalTotal;
    private LocalDateTime paidAt;

    protected BillRecord() {
    }

    public BillRecord(String orderId, int tableNumber, String pricingStrategy, double subtotal, double discountedTotal, double taxAmount, double tipAmount, int splitCount, double splitAmount, double finalTotal) {
        this.orderId = orderId;
        this.tableNumber = tableNumber;
        this.pricingStrategy = pricingStrategy;
        this.subtotal = subtotal;
        this.discountedTotal = discountedTotal;
        this.taxAmount = taxAmount;
        this.tipAmount = tipAmount;
        this.splitCount = splitCount;
        this.splitAmount = splitAmount;
        this.finalTotal = finalTotal;
        this.paidAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getOrderId() { return orderId; }
    public int getTableNumber() { return tableNumber; }
    public String getPricingStrategy() { return pricingStrategy; }
    public double getSubtotal() { return subtotal; }
    public double getDiscountedTotal() { return discountedTotal; }
    public double getTaxAmount() { return taxAmount; }
    public double getTipAmount() { return tipAmount; }
    public int getSplitCount() { return splitCount; }
    public double getSplitAmount() { return splitAmount; }
    public double getFinalTotal() { return finalTotal; }
    public LocalDateTime getPaidAt() { return paidAt; }
}
