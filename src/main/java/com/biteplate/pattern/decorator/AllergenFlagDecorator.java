package com.biteplate.pattern.decorator;

import com.biteplate.domain.menu.MenuItem;

public class AllergenFlagDecorator extends MenuCustomisationDecorator {
    private final String allergenNote;

    public AllergenFlagDecorator(MenuItem baseItem, String allergenNote) {
        super(baseItem, "Allergen Note", 0.0);
        this.allergenNote = allergenNote;
    }

    public String getAllergenNote() {
        return allergenNote;
    }

    @Override
    public String display() {
        return super.display() + " | ALLERGY: " + allergenNote;
    }
}
