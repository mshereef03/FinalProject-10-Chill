package com.chill.catalog.pricing;

import com.chill.catalog.model.MysteryBag;
import org.springframework.stereotype.Component;

@Component
public class FixedPriceStrategy implements PriceStrategy {

    @Override
    public double price(MysteryBag bag) {
        return bag.getPrice();
    }

    @Override
    public String code() {
        return "fixed";
    }
}
