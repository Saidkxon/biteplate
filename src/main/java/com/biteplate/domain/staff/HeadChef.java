package com.biteplate.domain.staff;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("HEAD_CHEF")
public class HeadChef extends Staff {
    protected HeadChef() {
    }

    public HeadChef(String staffId, String username, String password, String name) {
        super(staffId, username, password, name, StaffRole.HEAD_CHEF);
    }
}
