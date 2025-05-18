package com.chill.order.pricing;

import com.chill.order.model.MysteryBag;
import org.springframework.stereotype.Component;

@Component
public class DynamicPriceStrategy implements PriceStrategy{
    @Override
    public double price(MysteryBag bag) {
        return bag.getBasePrice() * 0.8;
    }

    @Override
    public String code() {
        return "dynamic";
    }
}
