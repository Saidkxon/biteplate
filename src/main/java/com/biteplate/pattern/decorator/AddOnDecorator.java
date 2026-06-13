package com.biteplate.pattern.decorator;

import com.biteplate.domain.menu.MenuItem;

public class AddOnDecorator extends MenuCustomisationDecorator {
    public AddOnDecorator(MenuItem baseItem, String addOnName, double addOnPrice) {
        super(baseItem, addOnName, addOnPrice);
    }
}
