package com.biteplate.service;

import com.biteplate.domain.menu.MenuItem;
import com.biteplate.repository.MenuItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {
    private final MenuItemRepository menuRepository;

    public MenuService(MenuItemRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public List<MenuItem> allItems() {
        return menuRepository.findAll();
    }
}
