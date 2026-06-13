package com.biteplate.repository;

import com.biteplate.domain.table.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.biteplate.domain.table.TableStatus;

public interface TableRepository extends JpaRepository<RestaurantTable, Integer> {
    long countByStatus(TableStatus status);
}
