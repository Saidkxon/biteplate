package com.biteplate.pattern.factory;

import com.biteplate.domain.menu.*;

public abstract class MenuItemFactory {
    public abstract MenuItem create(String id, String name, double price);
}
