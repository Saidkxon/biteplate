package com.biteplate.controller;

import com.biteplate.dto.BillRequest;
import com.biteplate.dto.BillResponse;
import com.biteplate.service.BillingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/billing")
public class BillingController {
    private final BillingService billingService;
    public BillingController(BillingService billingService) { this.billingService = billingService; }

    @PostMapping("/{orderId}")
    public BillResponse bill(@PathVariable String orderId, @Valid @RequestBody BillRequest request) {
        return billingService.bill(orderId, request.pricingType(), request.tipAmount(), request.splitCount());
    }
}
