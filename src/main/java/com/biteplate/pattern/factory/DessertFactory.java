package com.biteplate.pattern.factory;

import com.biteplate.domain.menu.Dessert;
import com.biteplate.domain.menu.MenuItem;

public class DessertFactory extends MenuItemFactory {
    @Override
    public MenuItem create(String id, String name, double price) {
        return new Dessert(id, name, price);
    }
}
