package com.biteplate.pattern.pricing;

import com.biteplate.domain.order.CustomerOrder;

public interface PricingStrategy {
    double calculateTotal(CustomerOrder order);
    String getName();
}
