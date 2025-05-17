package com.chill.order.service.CommandPattern;

import com.chill.order.model.Order;
import com.chill.order.repository.OrderRepository;
import com.chill.order.service.OrderService;

public class CancelOrderCommand implements Command {
    private final OrderRepository orderRepository;
    private int orderId;
    private Order order;

    public CancelOrderCommand(OrderRepository orderRepository, Order order) {
        this.orderRepository = orderRepository;
        this.order = order;
    }
    public CancelOrderCommand(OrderRepository orderRepository,int orderId) {
        this.orderRepository = orderRepository;
        this.orderId = orderId;
    }

    @Override
    public void execute() {
        orderRepository.deleteById(orderId);
    }

    @Override
    public void undo() {
        orderRepository.save(order);
    }
}
