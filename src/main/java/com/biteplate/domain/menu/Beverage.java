package com.biteplate.domain.menu;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("BEVERAGE")
public class Beverage extends MenuItem {
    protected Beverage() {
    }

    public Beverage(String id, String name, double price) {
        super(id, name, price, MenuCategory.BEVERAGE);
    }
}
