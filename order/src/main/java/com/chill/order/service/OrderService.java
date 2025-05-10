package com.chill.order.service;

import com.chill.order.model.Order;
import com.chill.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService { // this acts as my receiver for the command pattern

    @Autowired
    private OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    public Order placeOrder(Order order) {
        System.out.println("Order placed: " + order);
        return orderRepository.save(order);
    }
    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order getOrderById(int orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }
    public void cancelOrder(int orderId) {
         orderRepository.deleteById(orderId);

    }
}
