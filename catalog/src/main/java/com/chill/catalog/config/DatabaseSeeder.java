package com.chill.catalog.config;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.chill.catalog.model.MysteryBag;
import com.chill.catalog.repository.MysteryBagRepository;
import com.chill.catalog.service.MysteryBagService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.chill.catalog.model.MenuItem;
import com.chill.catalog.repository.MenuItemRepository;

@Configuration
public class DatabaseSeeder {
    
    private static final Logger logger = Logger.getLogger(DatabaseSeeder.class.getName());

    @Bean
    @Profile("!prod") // Don't run in production
    public CommandLineRunner initDatabase(MenuItemRepository menuItemRepository, MysteryBagRepository mysteryBagRepository,
                                          MysteryBagService mysteryBagService) {
        return args -> {
            // Clear existing data to avoid duplicates
            menuItemRepository.deleteAll();
            
            // Create sample menu items
            List<MenuItem> menuItems = Arrays.asList(
                // Appetizers
                new MenuItem(null, "Garlic Bread", "Freshly baked bread with garlic butter and herbs", 
                        5.99, "appetizer", 50),
                new MenuItem(null, "Mozzarella Sticks", "Deep-fried mozzarella sticks with marinara sauce", 
                        7.99, "appetizer", 70),
                new MenuItem(null, "Chicken Wings", "Spicy buffalo wings with blue cheese dip", 
                        9.99, "appetizer", 100),
                
                // Main Courses
                new MenuItem(null, "Margherita Pizza", "Classic pizza with tomato sauce, mozzarella, and basil", 
                        12.99, "main", 3),
                new MenuItem(null, "Pepperoni Pizza", "Pizza with tomato sauce, mozzarella, and pepperoni", 
                        14.99, "main", 4),
                new MenuItem(null, "Spaghetti Carbonara", "Spaghetti with creamy sauce, pancetta, and parmesan", 
                        13.99, "main",5),
                new MenuItem(null, "Grilled Chicken Salad", "Fresh salad with grilled chicken, mixed greens, and vinaigrette", 
                        11.99, "main",4),
                new MenuItem(null, "Beef Burger", "Juicy beef patty with lettuce, tomato, cheese, and special sauce", 
                        10.99, "main",2),
                
                // Desserts
                new MenuItem(null, "Chocolate Cake", "Rich chocolate cake with chocolate ganache", 
                        6.99, "dessert",10),
                new MenuItem(null, "Tiramisu", "Classic Italian dessert with coffee-soaked ladyfingers and mascarpone", 
                        7.99, "dessert",10),
                new MenuItem(null, "Cheesecake", "Creamy New York style cheesecake with berry compote", 
                        8.99, "dessert",10),
                
                // Beverages
                new MenuItem(null, "Soda", "Choice of Coke, Sprite, or Fanta", 
                        2.99, "beverage",100),
                new MenuItem(null, "Iced Tea", "Freshly brewed iced tea with lemon", 
                        3.99, "beverage",10),
                new MenuItem(null, "Coffee", "Premium blend coffee, served hot", 
                        4.99, "beverage",100)
            );
            
            // Save all menu items
            menuItemRepository.saveAll(menuItems);
            
            logger.info("Sample menu items have been added to the database 'catalog'");
            logger.info("Added " + menuItemRepository.count() + " menu items");

            // -- Seed a MysteryBag that references a few of those items ---
            mysteryBagRepository.deleteAll();
            // pick a few of the just-saved items:
            List<String> someIds = menuItemRepository.findAll().subList(0, 3)
                    .stream().map(MenuItem::getId).toList();

            MysteryBag bag = new MysteryBag();
            bag.setItemIds(someIds);
            bag.setBasePrice(9.99);
            bag.setSize(MysteryBag.Size.MEDIUM);
            // status is PENDING by default
            bag = mysteryBagRepository.save(bag);
            logger.info(">>> Seeded MysteryBag with ID " + bag.getId() + " referring to items " + someIds);

            // --- 3) Test publishing it right away ---
            MysteryBag published = mysteryBagService.publishMysteryBag(bag.getId());
            logger.info(">>> Published bag " + published.getId() );
            logger.info(">>> Post-publish status: " + published.getStatus());

            // --- 4) Log remaining quantities of those items ---
            someIds.forEach(itemId -> {
                menuItemRepository.findById(itemId).ifPresent(i ->
                        logger.info("    • " + i.getName() + " now has qty=" + i.getQuantity())
                );
            });
        
        };
    }
} 