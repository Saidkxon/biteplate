package com.biteplate.service;

import com.biteplate.domain.order.CustomerOrder;
import com.biteplate.domain.order.OrderStatus;
import com.biteplate.domain.staff.Staff;
import com.biteplate.domain.staff.StaffRole;
import com.biteplate.pattern.command.CancelOrderCommand;
import com.biteplate.pattern.command.KitchenQueue;
import com.biteplate.repository.OrderRepository;
import com.biteplate.repository.StaffRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KitchenService {
    private final KitchenQueue kitchenQueue;
    private final OrderRepository orderRepository;
    private final StaffRepository staffRepository;
    private final AuditService auditService;

    public KitchenService(KitchenQueue kitchenQueue, OrderRepository orderRepository, StaffRepository staffRepository, AuditService auditService) {
        this.kitchenQueue = kitchenQueue;
        this.orderRepository = orderRepository;
        this.staffRepository = staffRepository;
        this.auditService = auditService;
    }

    public List<String> queue() { return kitchenQueue.showQueue(); }

    public String processNext() {
        String result = kitchenQueue.processNext();
        auditService.record("KITCHEN_PROCESS", "QUEUE", result, "C001");
        return result;
    }

    public String undo() {
        String result = kitchenQueue.undoLast();
        auditService.record("KITCHEN_UNDO", "QUEUE", result, "C001");
        return result;
    }

    public String reprioritise(String orderId) {
        String result = kitchenQueue.reprioritise(orderId);
        auditService.record("KITCHEN_REPRIORITISE", orderId, result, "C001");
        return result;
    }

    public String cancel(String orderId) {
        CustomerOrder order = orderRepository.findById(orderId.toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Order not found."));

        if (order.getStatus() == OrderStatus.PAID) throw new IllegalStateException("Paid orders cannot be cancelled.");

        Staff chef = staffRepository.findFirstByRole(StaffRole.HEAD_CHEF)
                .orElseThrow(() -> new IllegalArgumentException("Head chef not found."));

        kitchenQueue.addCommand(new CancelOrderCommand(chef, order));
        auditService.record("ORDER_CANCEL_QUEUED", order.getOrderId(), "Cancel command queued", chef.getStaffId());
        return "Cancel command queued for " + order.getOrderId();
    }
}
