package com.biteplate.service;

import com.biteplate.domain.menu.MenuItem;
import com.biteplate.domain.order.CustomerOrder;
import com.biteplate.domain.order.OrderItem;
import com.biteplate.domain.order.OrderStatus;
import com.biteplate.domain.staff.Staff;
import com.biteplate.domain.staff.StaffRole;
import com.biteplate.domain.table.RestaurantTable;
import com.biteplate.domain.table.TableStatus;
import com.biteplate.dto.AddOrderItemRequest;
import com.biteplate.dto.CreateOrderItemRequest;
import com.biteplate.dto.CreateOrderRequest;
import com.biteplate.pattern.command.KitchenQueue;
import com.biteplate.pattern.command.PrepareOrderCommand;
import com.biteplate.pattern.observer.NotificationCenter;
import com.biteplate.repository.MenuItemRepository;
import com.biteplate.repository.OrderRepository;
import com.biteplate.repository.StaffRepository;
import com.biteplate.repository.TableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final MenuItemRepository menuRepository;
    private final TableRepository tableRepository;
    private final StaffRepository staffRepository;
    private final KitchenQueue kitchenQueue;
    private final NotificationCenter notificationCenter;
    private final AuditService auditService;

    public OrderService(OrderRepository orderRepository, MenuItemRepository menuRepository, TableRepository tableRepository, StaffRepository staffRepository, KitchenQueue kitchenQueue, NotificationCenter notificationCenter, AuditService auditService) {
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
        this.tableRepository = tableRepository;
        this.staffRepository = staffRepository;
        this.kitchenQueue = kitchenQueue;
        this.notificationCenter = notificationCenter;
        this.auditService = auditService;
    }

    @Transactional
    public CustomerOrder create(CreateOrderRequest request) {
        RestaurantTable table = tableRepository.findById(request.tableNumber())
                .orElseThrow(() -> new IllegalArgumentException("Table not found."));

        if (table.getStatus() != TableStatus.OCCUPIED) {
            throw new IllegalStateException("Seat the customer before creating an order.");
        }

        Staff waiter = staffRepository.findById(request.staffId().toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Staff not found."));

        String orderId = "ORD" + String.format("%03d", orderRepository.count() + 1);
        CustomerOrder order = new CustomerOrder(orderId, table.getTableNumber(), waiter.getStaffId());

        for (CreateOrderItemRequest itemRequest : request.items()) {
            addItemToOrderObject(order, itemRequest.menuItemId(), itemRequest.quantity(), itemRequest.addOns(), itemRequest.substitutions(), itemRequest.allergenNotes());
        }

        order.setStatus(OrderStatus.SENT_TO_KITCHEN);
        CustomerOrder saved = orderRepository.save(order);

        Staff chef = staffRepository.findFirstByRole(StaffRole.HEAD_CHEF)
                .orElseThrow(() -> new IllegalArgumentException("Head chef not found."));
        kitchenQueue.addCommand(new PrepareOrderCommand(chef, saved));

        if (saved.hasAllergenAlert()) {
            notificationCenter.notifyObservers("ALLERGY_ALERT", "Allergy note detected in order " + saved.getOrderId());
        }

        auditService.record("ORDER_CREATED", saved.getOrderId(), "Order created and sent to kitchen", waiter.getStaffId());
        return saved;
    }

    @Transactional
    public CustomerOrder addItem(String orderId, AddOrderItemRequest request) {
        CustomerOrder order = get(orderId);
        addItemToOrderObject(order, request.menuItemId(), request.quantity(), request.addOns(), request.substitutions(), request.allergenNotes());
        CustomerOrder saved = orderRepository.save(order);

        if (saved.hasAllergenAlert()) {
            notificationCenter.notifyObservers("ALLERGY_ALERT", "Allergy note detected in modified order " + saved.getOrderId());
        }

        auditService.record("ORDER_MODIFIED", saved.getOrderId(), "Item added before preparation", saved.getStaffId());
        return saved;
    }

    @Transactional
    public CustomerOrder removeLastItem(String orderId) {
        CustomerOrder order = get(orderId);
        order.removeLastItem();
        auditService.record("ORDER_MODIFIED", order.getOrderId(), "Last item removed before preparation", order.getStaffId());
        return orderRepository.save(order);
    }

    private void addItemToOrderObject(CustomerOrder order, String itemId, int quantity, String addOns, String substitutions, String allergenNotes) {
        MenuItem menuItem = menuRepository.findById(itemId.toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Menu item not found: " + itemId));
        order.addItem(new OrderItem(menuItem, quantity, addOns, substitutions, allergenNotes));
    }

    public List<CustomerOrder> allOrders() {
        return orderRepository.findAll();
    }

    public CustomerOrder get(String orderId) {
        return orderRepository.findById(orderId.toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Order not found."));
    }
}
