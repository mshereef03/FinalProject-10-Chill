package com.chill.catalog.pricing;

import com.chill.catalog.model.MysteryBag;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class TieredPriceStrategy implements PriceStrategy {

    @Override
    public double price(MysteryBag bag) {
        Instant now = Instant.now();
        int minutes = (int) Duration.between(bag.getReleaseAt(), now).toMinutes();
        double factor = minutes == 0 ? 1 :
                        minutes == 1 ? 0.9 :
                        0.75;

        return bag.getBasePrice() * factor;
    }

    @Override
    public String code() {
        return "tiered";
    }
}
