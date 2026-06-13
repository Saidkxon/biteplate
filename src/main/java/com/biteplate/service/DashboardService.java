package com.biteplate.service;

import com.biteplate.domain.order.OrderStatus;
import com.biteplate.domain.table.TableStatus;
import com.biteplate.dto.DashboardStats;
import com.biteplate.pattern.singleton.OrderHistoryLog;
import com.biteplate.repository.*;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
    private final TableRepository tableRepository;
    private final OrderRepository orderRepository;
    private final BillRecordRepository billRepository;
    private final ReservationRepository reservationRepository;
    private final NotificationRepository notificationRepository;
    private final OrderHistoryRecordRepository orderHistoryRepository;

    public DashboardService(
            TableRepository tableRepository,
            OrderRepository orderRepository,
            BillRecordRepository billRepository,
            ReservationRepository reservationRepository,
            NotificationRepository notificationRepository,
            OrderHistoryRecordRepository orderHistoryRepository
    ) {
        this.tableRepository = tableRepository;
        this.orderRepository = orderRepository;
        this.billRepository = billRepository;
        this.reservationRepository = reservationRepository;
        this.notificationRepository = notificationRepository;
        this.orderHistoryRepository = orderHistoryRepository;
    }

    public DashboardStats stats() {
        long activeOrders = orderRepository.count() - orderRepository.countByStatus(OrderStatus.PAID) - orderRepository.countByStatus(OrderStatus.CANCELLED);
        String topItem = OrderHistoryLog.getInstance()
                .mostFrequentlyOrderedItem(orderHistoryRepository)
                .orElse("No order history yet");

        return new DashboardStats(
                tableRepository.count(),
                tableRepository.countByStatus(TableStatus.RESERVED),
                tableRepository.countByStatus(TableStatus.OCCUPIED),
                activeOrders,
                orderRepository.countByStatus(OrderStatus.PAID),
                reservationRepository.count(),
                notificationRepository.countByReadFalse(),
                billRepository.totalRevenue(),
                topItem
        );
    }
}
