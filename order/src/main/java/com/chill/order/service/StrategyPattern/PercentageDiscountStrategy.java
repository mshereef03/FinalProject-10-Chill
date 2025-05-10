package com.chill.order.service.StrategyPattern;

public class PercentageDiscountStrategy implements DiscountStrategy {
    @Override
    public double applyDiscount(double price, double discount) {
        if (discount < 0 || discount > 100) {
            throw new IllegalArgumentException("Discount must be between 0 and 100");
        }
        return price - (price * discount / 100);
    }
}
