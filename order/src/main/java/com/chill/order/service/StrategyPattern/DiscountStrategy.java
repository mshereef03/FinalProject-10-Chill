package com.chill.order.service.StrategyPattern;

public interface DiscountStrategy {
    double applyDiscount(double price, double discount);
}
