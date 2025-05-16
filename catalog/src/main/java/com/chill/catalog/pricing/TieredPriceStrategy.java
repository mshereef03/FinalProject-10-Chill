package com.chill.catalog.pricing;

import com.chill.catalog.model.MysteryBag;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class TieredPriceStrategy implements PriceStrategy {

    @Override
    public double price(MysteryBag bag) {
        double factor = bag.getSize() == MysteryBag.Size.BIG ? 1 :
                        bag.getSize() == MysteryBag.Size.MEDIUM ? 15 :
                        0.75;

        return bag.getBasePrice() * factor;
    }

    @Override
    public String code() {
        return "tiered";
    }
}
