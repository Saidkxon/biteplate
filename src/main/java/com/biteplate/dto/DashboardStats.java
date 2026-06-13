package com.biteplate.dto;

public record DashboardStats(
        long totalTables,
        long reservedTables,
        long occupiedTables,
        long activeOrders,
        long paidOrders,
        long reservations,
        long unreadNotifications,
        double totalRevenue,
        String topItem
) {
}
