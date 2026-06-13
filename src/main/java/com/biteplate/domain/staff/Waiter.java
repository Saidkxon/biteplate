package com.biteplate.domain.staff;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("WAITER")
public class Waiter extends Staff {
    protected Waiter() {
    }

    public Waiter(String staffId, String username, String password, String name) {
        super(staffId, username, password, name, StaffRole.WAITER);
    }
}
