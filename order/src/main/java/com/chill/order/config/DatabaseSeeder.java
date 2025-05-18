package com.chill.order.config;

import com.chill.order.model.Cart;
import com.chill.order.model.PromoCode;
import com.chill.order.repository.CartRepository;
import com.chill.order.repository.PromoCodeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Configuration
public class DatabaseSeeder {

    private static final Logger logger = Logger.getLogger(DatabaseSeeder.class.getName());

    @Bean
    @Profile("!prod") // Don't run in production
    public CommandLineRunner initDatabase(CartRepository cartRepository, PromoCodeRepository promoCodeRepository) {
        return args -> {
            logger.info("🧹 Clearing old data...");
            cartRepository.deleteAll();
            promoCodeRepository.deleteAll();

            // Seed Cart
            Cart cart = new Cart();
            Cart savedCart = cartRepository.save(cart);
            logger.info("🛒 Seeded Cart with ID: " + savedCart.getId());

            // Seed PromoCodes
            List<PromoCode> promoCodes = Arrays.asList(
                    new PromoCode("WELCOME10", 10),
                    new PromoCode("SUMMER15", 15),
                    new PromoCode("EXPIRED20", 20)
            );

            promoCodeRepository.saveAll(promoCodes);

            logger.info("🏷️ Seeded PromoCodes:");
            promoCodes.forEach(p -> logger.info(" - " + p.getCode() + " (" + p.getDiscount() + "%)"));
        };
    }
}
