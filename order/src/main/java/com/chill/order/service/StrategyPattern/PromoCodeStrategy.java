package com.chill.order.service.StrategyPattern;

import com.chill.order.model.Order;

public class PromoCodeStrategy implements DiscountStrategy {
    @Override
    public double applyDiscount(Order order, double discount) {
        if (discount < 0 || discount > 100) {
            throw new IllegalArgumentException("Discount must be between 0 and 100");
        }
        //price
        return order.getPrice() - (order.getPrice() * discount / 100);
    }
}
