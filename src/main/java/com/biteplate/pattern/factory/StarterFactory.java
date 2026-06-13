package com.biteplate.pattern.factory;

import com.biteplate.domain.menu.Starter;
import com.biteplate.domain.menu.MenuItem;

public class StarterFactory extends MenuItemFactory {
    @Override
    public MenuItem create(String id, String name, double price) {
        return new Starter(id, name, price);
    }
}
