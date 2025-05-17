package com.chill.catalog.pricing;

import com.chill.catalog.model.MysteryBag;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class TieredPriceStrategy implements PriceStrategy {

    @Override
    public double price(MysteryBag bag) {
        return bag.getSize() == MysteryBag.Size.BIG ? 15 :
                        bag.getSize() == MysteryBag.Size.MEDIUM ? 10 :
                        8;
    }

    @Override
    public String code() {
        return "tiered";
    }
}
