package com.chill.catalog.pricing;

import com.chill.catalog.model.MysteryBag;

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
