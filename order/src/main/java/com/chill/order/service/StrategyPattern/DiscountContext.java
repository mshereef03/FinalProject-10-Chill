package com.chill.order.service.StrategyPattern;

import com.chill.order.model.Order;

public class DiscountContext {
    private DiscountStrategy discountStrategy;

    public DiscountContext(DiscountStrategy discountStrategy) {

        this.discountStrategy = discountStrategy;
    }

    public void setDiscountStrategy(DiscountStrategy discountStrategy) {

        this.discountStrategy = discountStrategy;
    }

    public double applyDiscount(Order order, double discount) {
        return discountStrategy.applyDiscount(order, discount);
    }

}
