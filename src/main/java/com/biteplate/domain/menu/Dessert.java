package com.biteplate.domain.menu;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("DESSERT")
public class Dessert extends MenuItem {
    protected Dessert() {
    }

    public Dessert(String id, String name, double price) {
        super(id, name, price, MenuCategory.DESSERT);
    }
}
