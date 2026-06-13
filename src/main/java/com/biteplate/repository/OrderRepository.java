package com.biteplate.repository;

import com.biteplate.domain.order.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import com.biteplate.domain.order.OrderStatus;

public interface OrderRepository extends JpaRepository<CustomerOrder, String> {
    long countByStatus(OrderStatus status);
}
