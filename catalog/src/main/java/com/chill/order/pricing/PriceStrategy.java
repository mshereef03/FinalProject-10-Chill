package com.chill.order.pricing;

import com.chill.order.model.MysteryBag;

public interface PriceStrategy {

    double price(MysteryBag bag);

    String code();
}
