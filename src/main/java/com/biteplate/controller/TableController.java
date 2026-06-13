package com.biteplate.controller;

import com.biteplate.domain.table.RestaurantTable;
import com.biteplate.service.TableService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
public class TableController {
    private final TableService tableService;
    public TableController(TableService tableService) { this.tableService = tableService; }

    @GetMapping
    public List<RestaurantTable> all() { return tableService.allTables(); }

    @PostMapping("/{tableNumber}/seat")
    public RestaurantTable seat(@PathVariable int tableNumber) { return tableService.seat(tableNumber); }

    @PostMapping("/{tableNumber}/clear")
    public RestaurantTable clear(@PathVariable int tableNumber) { return tableService.clear(tableNumber); }
}
