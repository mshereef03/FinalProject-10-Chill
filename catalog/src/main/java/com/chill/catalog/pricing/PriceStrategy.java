package com.chill.catalog.pricing;

import com.chill.catalog.model.MysteryBag;

public interface PriceStrategy {

    double price(MysteryBag bag);

    String code();
}
