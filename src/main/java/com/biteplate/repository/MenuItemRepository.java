package com.biteplate.repository;

import com.biteplate.domain.menu.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, String> {
}
