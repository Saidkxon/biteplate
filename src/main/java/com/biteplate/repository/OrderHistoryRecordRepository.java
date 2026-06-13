package com.biteplate.repository;

import com.biteplate.domain.history.OrderHistoryRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderHistoryRecordRepository extends JpaRepository<OrderHistoryRecord, Long> {
    List<OrderHistoryRecord> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    List<OrderHistoryRecord> findByTableNumber(int tableNumber);
}
