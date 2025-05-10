package com.chill.order.service.StrategyPattern;

public class FlatDiscountStrategy implements DiscountStrategy {
    @Override
    public double applyDiscount(double price, double discount) {
        if (discount < 0) {
            throw new IllegalArgumentException("Discount must be non-negative");
        }
        return price - discount;
    }
}

