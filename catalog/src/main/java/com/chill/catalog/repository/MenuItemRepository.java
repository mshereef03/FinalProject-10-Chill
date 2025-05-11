package com.chill.catalog.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.chill.catalog.model.MenuItem;

@Repository
public interface MenuItemRepository extends MongoRepository<MenuItem, String> {
    

} 