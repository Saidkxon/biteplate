package com.biteplate.controller;

import com.biteplate.domain.history.OrderHistoryRecord;
import com.biteplate.service.OrderHistoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
public class OrderHistoryController {
    private final OrderHistoryService historyService;

    public OrderHistoryController(OrderHistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping
    public List<OrderHistoryRecord> all() {
        return historyService.all();
    }

    @GetMapping("/table/{tableNumber}")
    public List<OrderHistoryRecord> byTable(@PathVariable int tableNumber) {
        return historyService.byTable(tableNumber);
    }

    @GetMapping("/top-item")
    public String topItem() {
        return historyService.topItem();
    }
}
