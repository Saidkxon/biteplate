package com.biteplate.pattern.pricing;

import org.springframework.stereotype.Component;

@Component
public class PricingStrategyFactory {
    public PricingStrategy create(String value) {
        PricingType type;
        try {
            type = PricingType.valueOf(value.toUpperCase());
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid pricing type. Use STANDARD, HAPPY_HOUR, LOYALTY_CARD, or GROUP_DISCOUNT.");
        }

        return switch (type) {
            case HAPPY_HOUR -> new HappyHourPricing();
            case LOYALTY_CARD -> new LoyaltyCardPricing();
            case GROUP_DISCOUNT -> new GroupDiscountPricing();
            case STANDARD -> new StandardPricing();
        };
    }
}
