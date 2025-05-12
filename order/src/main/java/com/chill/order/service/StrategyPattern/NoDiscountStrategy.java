package com.chill.order.service.StrategyPattern;

public class NoDiscountStrategy implements DiscountStrategy { // temp
    @Override
    public double applyDiscount(double price, double discount) {
        return price; // No discount applied
    }
}
