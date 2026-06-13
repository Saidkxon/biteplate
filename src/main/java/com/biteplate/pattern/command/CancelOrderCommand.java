package com.biteplate.pattern.command;

import com.biteplate.domain.order.CustomerOrder;
import com.biteplate.domain.order.OrderStatus;
import com.biteplate.domain.staff.Staff;

public class CancelOrderCommand implements KitchenCommand {
    private final Staff chef;
    private final CustomerOrder order;
    private OrderStatus previousStatus;

    public CancelOrderCommand(Staff chef, CustomerOrder order) {
        this.chef = chef;
        this.order = order;
    }

    public void execute() {
        previousStatus = order.getStatus();
        order.setStatus(OrderStatus.CANCELLED);
    }

    public void undo() {
        if (previousStatus != null) order.setStatus(previousStatus);
    }

    public String getOrderId() { return order.getOrderId(); }
    public CustomerOrder getOrder() { return order; }
    public String getDescription() { return "Cancel " + order.getOrderId() + " by " + chef.getName(); }
}
