package com.chill.catalog.pricing;

import com.chill.catalog.model.MysteryBag;
import org.springframework.stereotype.Component;

@Component
public class DynamicPriceStrategy implements PriceStrategy{
    @Override
    public double price(MysteryBag bag) {
        return bag.getPrice() * 0.8;
    }

    @Override
    public String code() {
        return "dynamic";
    }
}
