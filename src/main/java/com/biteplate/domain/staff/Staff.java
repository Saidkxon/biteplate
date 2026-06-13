package com.biteplate.domain.staff;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "staff")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "staff_type")
public abstract class Staff {
    @Id
    private String staffId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StaffRole role;

    protected Staff() {
    }

    protected Staff(String staffId, String username, String password, String name, StaffRole role) {
        if (staffId == null || staffId.isBlank()) throw new IllegalArgumentException("Staff ID is required.");
        if (username == null || username.isBlank()) throw new IllegalArgumentException("Username is required.");
        if (password == null || password.isBlank()) throw new IllegalArgumentException("Password is required.");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Staff name is required.");
        this.staffId = staffId;
        this.username = username;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public String getStaffId() { return staffId; }
    public String getUsername() { return username; }
    @JsonIgnore
    public String getPassword() { return password; }
    public String getName() { return name; }
    public StaffRole getRole() { return role; }

    public boolean canModifyKitchenOrders() {
        return role == StaffRole.HEAD_CHEF || role == StaffRole.MANAGER;
    }

    public boolean canAccessBilling() {
        return role == StaffRole.CASHIER || role == StaffRole.MANAGER;
    }

    public boolean canManageReservations() {
        return role == StaffRole.WAITER || role == StaffRole.MANAGER;
    }

    public boolean canViewAudit() {
        return role == StaffRole.MANAGER;
    }
}
