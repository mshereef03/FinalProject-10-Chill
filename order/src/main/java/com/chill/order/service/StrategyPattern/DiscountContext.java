package com.chill.order.service.StrategyPattern;

public class DiscountContext {
    private DiscountStrategy discountStrategy;

    public DiscountContext(DiscountStrategy discountStrategy) {
        this.discountStrategy = discountStrategy;
    }
    public void setDiscountStrategy(DiscountStrategy discountStrategy) {
        this.discountStrategy = discountStrategy;
    }

    public double applyDiscount(double price, double discount) {
        return discountStrategy.applyDiscount(price, discount);
    }

}
