package com.biteplate.domain.menu;

import jakarta.persistence.*;

@Entity
@Table(name = "menu_items")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "item_type")
public abstract class MenuItem {
    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MenuCategory category;

    protected MenuItem() {
    }

    protected MenuItem(String id, String name, double price, MenuCategory category) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Menu item ID is required.");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Menu item name is required.");
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative.");
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public MenuCategory getCategory() { return category; }

    public String display() {
        return id + " - " + name + " (" + category + ") $" + String.format("%.2f", price);
    }
}
