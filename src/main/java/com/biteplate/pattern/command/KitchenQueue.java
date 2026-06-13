package com.biteplate.pattern.command;

import com.biteplate.domain.order.CustomerOrder;
import com.biteplate.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class KitchenQueue {
    private final Deque<KitchenCommand> queue = new ArrayDeque<>();
    private final Deque<KitchenCommand> history = new ArrayDeque<>();
    private final OrderRepository orderRepository;

    public KitchenQueue(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public synchronized void addCommand(KitchenCommand command) {
        if (command == null) throw new IllegalArgumentException("Command cannot be null.");
        queue.offer(command);
    }

    public synchronized String processNext() {
        KitchenCommand command = queue.poll();
        if (command == null) return "Kitchen queue is empty.";
        command.execute();
        saveCommandOrder(command);
        history.push(command);
        return "Processed: " + command.getDescription();
    }

    public synchronized String undoLast() {
        KitchenCommand command = history.poll();
        if (command == null) return "No kitchen action to undo.";
        command.undo();
        saveCommandOrder(command);
        queue.offerFirst(command);
        return "Undone: " + command.getDescription();
    }

    public synchronized String reprioritise(String orderId) {
        KitchenCommand target = null;
        for (KitchenCommand command : queue) {
            if (command.getOrderId().equalsIgnoreCase(orderId)) {
                target = command;
                break;
            }
        }
        if (target == null) return "Order is not waiting in the kitchen queue.";
        queue.remove(target);
        queue.offerFirst(target);
        return "Order " + orderId.toUpperCase() + " moved to the front of the kitchen queue.";
    }

    public synchronized List<String> showQueue() {
        return queue.stream().map(KitchenCommand::getDescription).toList();
    }

    private void saveCommandOrder(KitchenCommand command) {
        CustomerOrder order = null;
        if (command instanceof PrepareOrderCommand prepare) order = prepare.getOrder();
        if (command instanceof CancelOrderCommand cancel) order = cancel.getOrder();
        if (order != null) orderRepository.save(order);
    }
}
