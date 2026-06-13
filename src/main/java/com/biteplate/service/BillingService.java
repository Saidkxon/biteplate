package com.biteplate.service;

import com.biteplate.domain.billing.Bill;
import com.biteplate.domain.billing.BillRecord;
import com.biteplate.domain.order.CustomerOrder;
import com.biteplate.domain.order.OrderStatus;
import com.biteplate.domain.table.RestaurantTable;
import com.biteplate.dto.BillResponse;
import com.biteplate.pattern.pricing.PricingStrategy;
import com.biteplate.pattern.pricing.PricingStrategyFactory;
import com.biteplate.pattern.singleton.OrderHistoryLog;
import com.biteplate.repository.BillRecordRepository;
import com.biteplate.repository.OrderHistoryRecordRepository;
import com.biteplate.repository.OrderRepository;
import com.biteplate.repository.TableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BillingService {
    private final OrderRepository orderRepository;
    private final TableRepository tableRepository;
    private final BillRecordRepository billRepository;
    private final OrderHistoryRecordRepository orderHistoryRepository;
    private final PricingStrategyFactory pricingFactory;
    private final AuditService auditService;

    public BillingService(
            OrderRepository orderRepository,
            TableRepository tableRepository,
            BillRecordRepository billRepository,
            OrderHistoryRecordRepository orderHistoryRepository,
            PricingStrategyFactory pricingFactory,
            AuditService auditService
    ) {
        this.orderRepository = orderRepository;
        this.tableRepository = tableRepository;
        this.billRepository = billRepository;
        this.orderHistoryRepository = orderHistoryRepository;
        this.pricingFactory = pricingFactory;
        this.auditService = auditService;
    }

    @Transactional
    public BillResponse bill(String orderId, String pricingType, double tipAmount, int splitCount) {
        CustomerOrder order = orderRepository.findById(orderId.toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Order not found."));

        if (order.getStatus() == OrderStatus.CANCELLED) throw new IllegalStateException("Cancelled order cannot be billed.");
        if (splitCount < 1) throw new IllegalArgumentException("Split count must be at least 1.");

        PricingStrategy strategy = pricingFactory.create(pricingType);
        Bill bill = new Bill(order, strategy, tipAmount, splitCount);

        double subtotal = bill.getSubtotal();
        double discounted = bill.getDiscountedTotal(order);
        double tax = bill.getTaxAmount(order);
        double finalTotal = bill.getFinalTotal(order);
        double splitAmount = bill.getSplitAmount(order);

        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);

        RestaurantTable table = tableRepository.findById(order.getTableNumber())
                .orElseThrow(() -> new IllegalArgumentException("Table not found."));
        table.markAwaitingBill();
        tableRepository.save(table);

        billRepository.save(new BillRecord(order.getOrderId(), order.getTableNumber(), strategy.getName(), subtotal, discounted, tax, tipAmount, splitCount, splitAmount, finalTotal));

        // Required Singleton Pattern: one globally accessible order history log.
        OrderHistoryLog.getInstance().append(orderHistoryRepository, order, finalTotal);

        auditService.record("BILL_GENERATED", order.getOrderId(), "Bill generated using " + strategy.getName(), order.getStaffId());

        return new BillResponse(order.getOrderId(), order.getTableNumber(), strategy.getName(), subtotal, discounted, tax, tipAmount, splitCount, splitAmount, finalTotal);
    }
}
