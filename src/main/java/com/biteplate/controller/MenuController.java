package com.biteplate.controller;

import com.biteplate.domain.menu.MenuItem;
import com.biteplate.service.MenuService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {
    private final MenuService menuService;
    public MenuController(MenuService menuService) { this.menuService = menuService; }

    @GetMapping
    public List<MenuItem> all() { return menuService.allItems(); }
}
