package com.biteplate.pattern.singleton;

import com.biteplate.domain.history.OrderHistoryRecord;
import com.biteplate.domain.order.CustomerOrder;
import com.biteplate.domain.order.OrderItem;
import com.biteplate.repository.OrderHistoryRecordRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public final class OrderHistoryLog implements Iterable<OrderHistoryRecord> {
    private static volatile OrderHistoryLog instance;
    private List<OrderHistoryRecord> snapshot = new ArrayList<>();

    private OrderHistoryLog() {
    }

    public static OrderHistoryLog getInstance() {
        if (instance == null) {
            synchronized (OrderHistoryLog.class) {
                if (instance == null) {
                    instance = new OrderHistoryLog();
                }
            }
        }
        return instance;
    }

    public synchronized OrderHistoryRecord append(OrderHistoryRecordRepository repository, CustomerOrder order, double finalTotal) {
        String itemSummary = order.getItems().stream()
                .map(OrderItem::display)
                .collect(Collectors.joining("; "));

        OrderHistoryRecord record = new OrderHistoryRecord(
                order.getOrderId(),
                order.getTableNumber(),
                order.getStaffId(),
                itemSummary,
                finalTotal,
                LocalDateTime.now()
        );
        OrderHistoryRecord saved = repository.save(record);
        refresh(repository);
        return saved;
    }

    public synchronized List<OrderHistoryRecord> findByDateRange(OrderHistoryRecordRepository repository, LocalDateTime start, LocalDateTime end) {
        return repository.findByTimestampBetween(start, end);
    }

    public synchronized List<OrderHistoryRecord> findByTable(OrderHistoryRecordRepository repository, int tableNumber) {
        return repository.findByTableNumber(tableNumber);
    }

    public synchronized Optional<String> mostFrequentlyOrderedItem(OrderHistoryRecordRepository repository) {
        refresh(repository);
        Map<String, Long> counts = new HashMap<>();
        for (OrderHistoryRecord record : snapshot) {
            if (record.getItemSummary() == null) continue;
            String[] parts = record.getItemSummary().split(";");
            for (String part : parts) {
                String cleaned = part.replaceAll("^\\s*\\d+\\s*x\\s*", "").replaceAll("\\s*=.*$", "").trim();
                if (!cleaned.isBlank()) counts.put(cleaned, counts.getOrDefault(cleaned, 0L) + 1);
            }
        }
        return counts.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey);
    }

    public synchronized void refresh(OrderHistoryRecordRepository repository) {
        snapshot = repository.findAll();
    }

    @Override
    public synchronized Iterator<OrderHistoryRecord> iterator() {
        return List.copyOf(snapshot).iterator();
    }
}
