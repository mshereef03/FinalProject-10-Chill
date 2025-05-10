package com.chill.order.service.CommandPattern;

import com.chill.order.model.Order;
import com.chill.order.service.OrderService;

public class PlaceOrderCommand implements Command {
    private final OrderService orderService;
    private int orderId;
    private Order order;

    public PlaceOrderCommand(OrderService orderService, Order order) {
        this.orderService = orderService;
        this.order = order;
    }
    public PlaceOrderCommand(OrderService orderService, int orderId) {
        this.orderService = orderService;
        this.orderId = orderId;
    }

    @Override
    public void execute() {
        orderService.placeOrder(order);
    }

    @Override
    public void undo() {
            orderService.cancelOrder(orderId);
    }
}
