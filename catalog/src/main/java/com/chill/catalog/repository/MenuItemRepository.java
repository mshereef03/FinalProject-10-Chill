package com.chill.catalog.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.chill.catalog.model.MenuItem;

@Repository
public interface MenuItemRepository extends MongoRepository<MenuItem, String> {
    
    // Find menu items by category
    List<MenuItem> findByCategory(String category);
    
    // Find menu items by name containing the given string (case insensitive)
    List<MenuItem> findByNameContainingIgnoreCase(String name);
} 