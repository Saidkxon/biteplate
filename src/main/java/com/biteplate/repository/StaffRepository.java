package com.biteplate.repository;

import com.biteplate.domain.staff.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import com.biteplate.domain.staff.StaffRole;

import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, String> {
    Optional<Staff> findByUsername(String username);
    Optional<Staff> findFirstByRole(StaffRole role);
}
