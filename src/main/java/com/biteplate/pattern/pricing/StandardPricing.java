package com.biteplate.pattern.pricing;

import com.biteplate.domain.order.CustomerOrder;

public class StandardPricing implements PricingStrategy {
    public double calculateTotal(CustomerOrder order) { return order.getSubtotal(); }
    public String getName() { return "Standard Pricing"; }
}
