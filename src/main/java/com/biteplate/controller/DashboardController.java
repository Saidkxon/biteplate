package com.biteplate.controller;

import com.biteplate.dto.DashboardStats;
import com.biteplate.service.DashboardService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;
    public DashboardController(DashboardService dashboardService) { this.dashboardService = dashboardService; }

    @GetMapping
    public DashboardStats stats() { return dashboardService.stats(); }
}
