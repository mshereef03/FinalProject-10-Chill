package com.chill.order.service.CommandPattern;

import com.chill.order.model.Order;
import com.chill.order.service.OrderService;

public class CancelOrderCommand implements Command {
    private final OrderService orderService;
    private int orderId;
    private Order order;

    public CancelOrderCommand(OrderService orderService, Order order) {
        this.orderService = orderService;
        this.order = order;
    }
    public CancelOrderCommand(OrderService orderService,int orderId) {
        this.orderService = orderService;
        this.orderId = orderId;
    }

    @Override
    public void execute() {
        orderService.cancelOrder(orderId);
    }

    @Override
    public void undo() {
        orderService.placeOrder(order);
    }
}
