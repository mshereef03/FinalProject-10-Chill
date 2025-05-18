package com.chill.order.service.CommandPattern;

import com.chill.order.model.Order;
import com.chill.order.repository.OrderRepository;
import com.chill.order.service.OrderService;

public class PlaceOrderCommand implements Command {
    private final OrderRepository orderRepository;
    private int orderId;
    private Order order;

    public PlaceOrderCommand(OrderRepository orderRepository, Order order) {
        this.orderRepository = orderRepository;
        this.order = order;
    }
    public PlaceOrderCommand(OrderRepository orderRepository, int orderId) {
        this.orderRepository = orderRepository;
        this.orderId = orderId;
    }

    @Override
    public void execute() {
        System.out.println("Cart ? place order command" + (order.getCart() == null ));
        orderRepository.save(order);
    }

    @Override
    public void undo() {
            orderRepository.deleteById(orderId);
    }
}
