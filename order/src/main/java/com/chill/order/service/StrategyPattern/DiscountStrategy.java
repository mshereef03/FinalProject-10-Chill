package com.chill.order.service.StrategyPattern;

import com.chill.order.model.Order;

public interface DiscountStrategy {
    double applyDiscount(Order order, double discount);
}
