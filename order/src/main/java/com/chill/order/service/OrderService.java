package com.chill.order.service;
import com.chill.order.model.Cart;
import com.chill.order.model.Order;
import com.chill.order.repository.CartRepository;
import com.chill.order.repository.OrderRepository;
import com.chill.order.service.CommandPattern.CancelOrderCommand;
import com.chill.order.service.CommandPattern.Invoker;
import com.chill.order.service.CommandPattern.PlaceOrderCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService { // this acts as my receiver for the command pattern

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;
    Invoker invoker= new Invoker();

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order placeOrder( Order order) {
        System.out.println("Cart ? place order service" + (order.getCart() == null ));
        if (order.getCart() != null) {
            Cart freshCart = cartRepository.findById(order.getCart().getId())
                    .orElseThrow(() -> new RuntimeException("Cart not found"));
            order.setCart(freshCart);
        }
        invoker.setCommand(new PlaceOrderCommand(orderRepository, order));
        invoker.executeCommand();
        return order;
    }
    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order getOrderById(int orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    public Order cancelOrder(int orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order not found!");
        }
        Order fetchedOrder= order.get();
        invoker.setCommand(new CancelOrderCommand(orderRepository, orderId));
        invoker.executeCommand();
        return fetchedOrder;
    }
}
