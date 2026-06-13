package com.biteplate.pattern.pricing;

import com.biteplate.domain.menu.MenuCategory;
import com.biteplate.domain.order.CustomerOrder;
import com.biteplate.domain.order.OrderItem;

public class LoyaltyCardPricing implements PricingStrategy {
    public double calculateTotal(CustomerOrder order) {
        double subtotal = order.getSubtotal();
        double cheapestDrink = order.getItems().stream()
                .filter(item -> item.getMenuItem().getCategory() == MenuCategory.BEVERAGE)
                .mapToDouble(OrderItem::getLineTotal)
                .min()
                .orElse(0.0);
        return Math.max(0, (subtotal - cheapestDrink) * 0.90);
    }
    public String getName() { return "Loyalty Card - 10% Off + Free Drink"; }
}
