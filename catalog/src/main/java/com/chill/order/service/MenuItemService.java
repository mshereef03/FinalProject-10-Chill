package com.chill.order.service;

import java.util.List;
import java.util.Optional;

import com.chill.order.model.MysteryBag;
import com.chill.order.observer.MysteryBagObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chill.order.model.MenuItem;
import com.chill.order.repository.MenuItemRepository;

@Service
public class MenuItemService implements MysteryBagObserver {

    private final MenuItemRepository menuItemRepository;

    @Autowired
    public MenuItemService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    // Create a new menu item
    public MenuItem createMenuItem(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    // Get all menu items
    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    // Get menu item by ID
    public Optional<MenuItem> getMenuItemById(String id) {
        return menuItemRepository.findById(id);
    }

    // Update a menu item
    public MenuItem updateMenuItem(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    // Delete a menu item
    public void deleteMenuItem(String id) {
        menuItemRepository.deleteById(id);
    }

    // Find menu items by category
    public List<MenuItem> filterMenuItemsByCategory(String category) {
        return menuItemRepository.findByCategory(category);
    }

    // Find menu items by name
    public List<MenuItem> searchMenuItemsByName(String name) {
        return menuItemRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public void onPublish(MysteryBag bag) {
        bag.getItemIds().forEach(itemId ->
                menuItemRepository.findById(itemId).ifPresent(menuItem -> {
                    menuItem.setQuantity(menuItem.getQuantity() - 1);
                    menuItemRepository.save(menuItem);
                })
        );
    }
} 