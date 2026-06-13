package com.biteplate.domain.staff;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CASHIER")
public class Cashier extends Staff {
    protected Cashier() {
    }

    public Cashier(String staffId, String username, String password, String name) {
        super(staffId, username, password, name, StaffRole.CASHIER);
    }
}
