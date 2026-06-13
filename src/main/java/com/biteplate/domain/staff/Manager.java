package com.biteplate.domain.staff;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("MANAGER")
public class Manager extends Staff {
    protected Manager() {
    }

    public Manager(String staffId, String username, String password, String name) {
        super(staffId, username, password, name, StaffRole.MANAGER);
    }
}
