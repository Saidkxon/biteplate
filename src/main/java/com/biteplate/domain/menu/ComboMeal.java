package com.biteplate.domain.menu;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("COMBO_MEAL")
public class ComboMeal extends MenuItem {
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "combo_meal_items",
            joinColumns = @JoinColumn(name = "combo_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<MenuItem> includedItems = new ArrayList<>();

    protected ComboMeal() {
    }

    public ComboMeal(String id, String name, double price) {
        super(id, name, price, MenuCategory.COMBO_MEAL);
    }

    public void addItem(MenuItem item) {
        if (item == null) throw new IllegalArgumentException("Combo item cannot be null.");
        includedItems.add(item);
    }

    public List<MenuItem> getIncludedItems() {
        return includedItems;
    }

    @Override
    public String display() {
        return getId() + " - " + getName() + " (Combo: " + includedItems.size() + " items) $" + String.format("%.2f", getPrice());
    }
}
