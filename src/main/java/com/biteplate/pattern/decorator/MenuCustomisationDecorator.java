package com.biteplate.pattern.decorator;

import com.biteplate.domain.menu.MenuCategory;
import com.biteplate.domain.menu.MenuItem;

public abstract class MenuCustomisationDecorator extends MenuItem {
    protected final MenuItem baseItem;

    protected MenuCustomisationDecorator(MenuItem baseItem, String label, double extraPrice) {
        super(baseItem.getId() + "-" + label.toUpperCase().replace(" ", "_"),
                baseItem.getName() + " + " + label,
                baseItem.getPrice() + extraPrice,
                baseItem.getCategory());
        this.baseItem = baseItem;
    }

    public MenuItem getBaseItem() { return baseItem; }
}
