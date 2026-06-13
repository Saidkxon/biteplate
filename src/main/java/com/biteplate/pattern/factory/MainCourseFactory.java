package com.biteplate.pattern.factory;

import com.biteplate.domain.menu.MainCourse;
import com.biteplate.domain.menu.MenuItem;

public class MainCourseFactory extends MenuItemFactory {
    @Override
    public MenuItem create(String id, String name, double price) {
        return new MainCourse(id, name, price);
    }
}
