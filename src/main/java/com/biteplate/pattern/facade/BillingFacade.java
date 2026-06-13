package com.biteplate.pattern.facade;

import com.biteplate.dto.BillResponse;
import com.biteplate.service.BillingService;

public class BillingFacade {
    private final BillingService billingService;

    public BillingFacade(BillingService billingService) {
        this.billingService = billingService;
    }

    public BillResponse closeOrder(String orderId, String pricingType, double tipAmount, int splitCount) {
        return billingService.bill(orderId, pricingType, tipAmount, splitCount);
    }
}
