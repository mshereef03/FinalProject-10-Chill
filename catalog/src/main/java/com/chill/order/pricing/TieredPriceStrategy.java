package com.chill.order.pricing;

import com.chill.order.model.MysteryBag;
import org.springframework.stereotype.Component;

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
