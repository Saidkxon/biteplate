package com.biteplate.domain.billing;

public class BillLineItem {
    private final String description;
    private final int quantity;
    private final double lineTotal;

    public BillLineItem(String description, int quantity, double lineTotal) {
        this.description = description;
        this.quantity = quantity;
        this.lineTotal = lineTotal;
    }

    public String getDescription() { return description; }
    public int getQuantity() { return quantity; }
    public double getLineTotal() { return lineTotal; }
}
