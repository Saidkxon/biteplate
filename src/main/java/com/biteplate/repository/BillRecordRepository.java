package com.biteplate.repository;

import com.biteplate.domain.billing.BillRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BillRecordRepository extends JpaRepository<BillRecord, Long> {
    @Query("select coalesce(sum(b.finalTotal), 0) from BillRecord b")
    double totalRevenue();
}
