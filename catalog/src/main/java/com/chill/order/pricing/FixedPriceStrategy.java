package com.chill.order.pricing;

import com.chill.order.model.MysteryBag;
import org.springframework.stereotype.Component;

@Component
public class FixedPriceStrategy implements PriceStrategy {

    @Override
    public double price(MysteryBag bag) {
        return bag.getBasePrice();
    }

    @Override
    public String code() {
        return "fixed";
    }
}
