package com.biteplate.domain.billing;

import com.biteplate.domain.order.CustomerOrder;
import com.biteplate.domain.order.OrderItem;
import com.biteplate.pattern.pricing.PricingStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bill {
    private static final double TAX_RATE = 0.12;

    private final String orderId;
    private final int tableNumber;
    private final List<BillLineItem> lineItems = new ArrayList<>();
    private final PricingStrategy pricingStrategy;
    private double tipAmount;
    private int splitCount;

    public Bill(CustomerOrder order, PricingStrategy pricingStrategy, double tipAmount, int splitCount) {
        if (order == null) throw new IllegalArgumentException("Order is required.");
        if (pricingStrategy == null) throw new IllegalArgumentException("Pricing strategy is required.");
        if (tipAmount < 0) throw new IllegalArgumentException("Tip cannot be negative.");
        if (splitCount < 1) throw new IllegalArgumentException("Split count must be at least 1.");
        this.orderId = order.getOrderId();
        this.tableNumber = order.getTableNumber();
        this.pricingStrategy = pricingStrategy;
        this.tipAmount = tipAmount;
        this.splitCount = splitCount;
        for (OrderItem item : order.getItems()) {
            lineItems.add(new BillLineItem(item.display(), item.getQuantity(), item.getLineTotal()));
        }
    }

    public String getOrderId() { return orderId; }
    public int getTableNumber() { return tableNumber; }
    public List<BillLineItem> getLineItems() { return Collections.unmodifiableList(lineItems); }
    public String getPricingStrategyName() { return pricingStrategy.getName(); }
    public double getSubtotal() { return lineItems.stream().mapToDouble(BillLineItem::getLineTotal).sum(); }
    public double getDiscountedTotal(CustomerOrder order) { return pricingStrategy.calculateTotal(order); }
    public double getTaxAmount(CustomerOrder order) { return getDiscountedTotal(order) * TAX_RATE; }
    public double getFinalTotal(CustomerOrder order) { return getDiscountedTotal(order) + getTaxAmount(order) + tipAmount; }
    public double getTipAmount() { return tipAmount; }
    public int getSplitCount() { return splitCount; }
    public double getSplitAmount(CustomerOrder order) { return getFinalTotal(order) / splitCount; }
}
