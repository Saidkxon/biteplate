package com.biteplate.domain.order;

import com.biteplate.domain.menu.MenuItem;
import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private MenuItem menuItem;

    @Column(nullable = false)
    private int quantity;

    @Column(length = 500)
    private String addOns;

    @Column(length = 500)
    private String substitutions;

    @Column(length = 500)
    private String allergenNotes;

    protected OrderItem() {
    }

    public OrderItem(MenuItem menuItem, int quantity, String addOns, String substitutions, String allergenNotes) {
        if (menuItem == null) throw new IllegalArgumentException("Menu item is required.");
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive.");
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.addOns = safe(addOns);
        this.substitutions = safe(substitutions);
        this.allergenNotes = safe(allergenNotes);
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }

    public Long getId() { return id; }
    public MenuItem getMenuItem() { return menuItem; }
    public int getQuantity() { return quantity; }
    public String getAddOns() { return addOns; }
    public String getSubstitutions() { return substitutions; }
    public String getAllergenNotes() { return allergenNotes; }

    public boolean hasAllergenAlert() {
        return allergenNotes != null && !allergenNotes.isBlank();
    }

    public double getLineTotal() {
        double addOnCharge = (addOns == null || addOns.isBlank()) ? 0 : 1.50;
        return (menuItem.getPrice() + addOnCharge) * quantity;
    }

    public String display() {
        String extras = "";
        if (addOns != null && !addOns.isBlank()) extras += " | Add-ons: " + addOns;
        if (substitutions != null && !substitutions.isBlank()) extras += " | Sub: " + substitutions;
        if (allergenNotes != null && !allergenNotes.isBlank()) extras += " | ALLERGY: " + allergenNotes;
        return quantity + " x " + menuItem.getName() + extras + " = $" + String.format("%.2f", getLineTotal());
    }
}
