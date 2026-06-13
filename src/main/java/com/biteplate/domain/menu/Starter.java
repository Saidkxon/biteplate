package com.biteplate.domain.menu;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("STARTER")
public class Starter extends MenuItem {
    protected Starter() {
    }

    public Starter(String id, String name, double price) {
        super(id, name, price, MenuCategory.STARTER);
    }
}
