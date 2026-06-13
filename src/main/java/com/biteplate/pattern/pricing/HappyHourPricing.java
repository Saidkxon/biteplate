package com.biteplate.pattern.pricing;

import com.biteplate.domain.order.CustomerOrder;

public class HappyHourPricing implements PricingStrategy {
    public double calculateTotal(CustomerOrder order) { return order.getSubtotal() * 0.80; }
    public String getName() { return "Happy Hour - 20% Off"; }
}
