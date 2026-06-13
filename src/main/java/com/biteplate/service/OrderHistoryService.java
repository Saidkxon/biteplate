package com.biteplate.service;

import com.biteplate.domain.history.OrderHistoryRecord;
import com.biteplate.pattern.singleton.OrderHistoryLog;
import com.biteplate.repository.OrderHistoryRecordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderHistoryService {
    private final OrderHistoryRecordRepository historyRepository;

    public OrderHistoryService(OrderHistoryRecordRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public List<OrderHistoryRecord> all() {
        OrderHistoryLog.getInstance().refresh(historyRepository);
        return historyRepository.findAll();
    }

    public List<OrderHistoryRecord> byTable(int tableNumber) {
        return OrderHistoryLog.getInstance().findByTable(historyRepository, tableNumber);
    }

    public List<OrderHistoryRecord> byDateRange(LocalDateTime start, LocalDateTime end) {
        return OrderHistoryLog.getInstance().findByDateRange(historyRepository, start, end);
    }

    public String topItem() {
        return OrderHistoryLog.getInstance().mostFrequentlyOrderedItem(historyRepository).orElse("No history yet");
    }
}
