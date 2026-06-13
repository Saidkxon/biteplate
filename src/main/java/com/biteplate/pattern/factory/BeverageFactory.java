package com.biteplate.pattern.factory;

import com.biteplate.domain.menu.Beverage;
import com.biteplate.domain.menu.MenuItem;

public class BeverageFactory extends MenuItemFactory {
    @Override
    public MenuItem create(String id, String name, double price) {
        return new Beverage(id, name, price);
    }
}
