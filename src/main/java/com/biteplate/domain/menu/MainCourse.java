package com.biteplate.domain.menu;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("MAIN_COURSE")
public class MainCourse extends MenuItem {
    protected MainCourse() {
    }

    public MainCourse(String id, String name, double price) {
        super(id, name, price, MenuCategory.MAIN_COURSE);
    }
}
