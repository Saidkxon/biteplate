package com.biteplate.pattern.pricing;

import com.biteplate.domain.order.CustomerOrder;

public class GroupDiscountPricing implements PricingStrategy {
    public double calculateTotal(CustomerOrder order) { return order.getSubtotal() * 0.85; }
    public String getName() { return "Group Discount - 15% Off"; }
}
