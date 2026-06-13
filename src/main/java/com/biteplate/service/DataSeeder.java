package com.biteplate.service;

import com.biteplate.domain.menu.*;
import com.biteplate.domain.staff.*;
import com.biteplate.domain.table.RestaurantTable;
import com.biteplate.repository.MenuItemRepository;
import com.biteplate.repository.StaffRepository;
import com.biteplate.repository.TableRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {
    private final MenuItemRepository menuRepository;
    private final StaffRepository staffRepository;
    private final TableRepository tableRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed.default-password:1234}")
    private String defaultPassword;

    public DataSeeder(MenuItemRepository menuRepository, StaffRepository staffRepository, TableRepository tableRepository, PasswordEncoder passwordEncoder) {
        this.menuRepository = menuRepository;
        this.staffRepository = staffRepository;
        this.tableRepository = tableRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void run(String... args) {
        if (menuRepository.count() == 0) {
            Starter s1 = new Starter("S1", "Garlic Bread", 4.50);
            Starter s2 = new Starter("S2", "Tomato Soup", 5.00);
            MainCourse m1 = new MainCourse("M1", "Classic Burger", 12.00);
            MainCourse m2 = new MainCourse("M2", "Grilled Chicken", 14.00);
            Dessert d1 = new Dessert("D1", "Cheesecake", 6.50);
            Dessert d2 = new Dessert("D2", "Chocolate Brownie", 6.00);
            Beverage b1 = new Beverage("B1", "Cola", 2.50);
            Beverage b2 = new Beverage("B2", "Orange Juice", 3.00);

            menuRepository.save(s1);
            menuRepository.save(s2);
            menuRepository.save(m1);
            menuRepository.save(m2);
            menuRepository.save(d1);
            menuRepository.save(d2);
            menuRepository.save(b1);
            menuRepository.save(b2);

            ComboMeal combo = new ComboMeal("C1", "Lunch Combo", 16.00);
            combo.addItem(s1);
            combo.addItem(m1);
            combo.addItem(b1);
            menuRepository.save(combo);
        }

        if (staffRepository.count() == 0) {
            String encodedPassword = passwordEncoder.encode(defaultPassword);
            staffRepository.save(new Waiter("W001", "waiter", encodedPassword, "Ali Waiter"));
            staffRepository.save(new HeadChef("C001", "chef", encodedPassword, "Sara Head Chef"));
            staffRepository.save(new Cashier("CA001", "cashier", encodedPassword, "Nora Cashier"));
            staffRepository.save(new Manager("MGR001", "manager", encodedPassword, "Omar Manager"));
        }

        if (tableRepository.count() == 0) {
            tableRepository.save(new RestaurantTable(1, 2));
            tableRepository.save(new RestaurantTable(2, 4));
            tableRepository.save(new RestaurantTable(3, 6));
            tableRepository.save(new RestaurantTable(4, 4));
            tableRepository.save(new RestaurantTable(5, 8));
        }
    }
}
