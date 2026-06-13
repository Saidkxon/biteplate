package com.biteplate.service;

import com.biteplate.domain.table.RestaurantTable;
import com.biteplate.repository.TableRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableService {
    private final TableRepository tableRepository;
    private final AuditService auditService;

    public TableService(TableRepository tableRepository, AuditService auditService) {
        this.tableRepository = tableRepository;
        this.auditService = auditService;
    }

    public List<RestaurantTable> allTables() {
        return tableRepository.findAll();
    }

    public RestaurantTable seat(int tableNumber) {
        RestaurantTable table = tableRepository.findById(tableNumber)
                .orElseThrow(() -> new IllegalArgumentException("Table not found."));
        table.seatCustomer();
        auditService.record("TABLE_SEATED", String.valueOf(tableNumber), "Customer seated at table " + tableNumber, "SYSTEM");
        return tableRepository.save(table);
    }

    public RestaurantTable clear(int tableNumber) {
        RestaurantTable table = tableRepository.findById(tableNumber)
                .orElseThrow(() -> new IllegalArgumentException("Table not found."));
        table.clearTable();
        auditService.record("TABLE_CLEARED", String.valueOf(tableNumber), "Table cleared " + tableNumber, "SYSTEM");
        return tableRepository.save(table);
    }
}
