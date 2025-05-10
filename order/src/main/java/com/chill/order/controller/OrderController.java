package com.chill.order.controller;

import com.chill.order.model.Order;
import com.chill.order.service.CommandPattern.Invoker;
import com.chill.order.service.CommandPattern.PlaceOrderCommand;
import com.chill.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/order")

public class OrderController {

    @Autowired
    private OrderService orderService;

    private Invoker invoker;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
        this.invoker = new Invoker();
    }
    @GetMapping("/{orderId}")
    public Order getOrderById(@PathVariable int orderId) {
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order not found!");
        }
        return order;
    }
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }
    @PostMapping
    public Order placeOrder(@RequestBody Order order) {
        invoker.setCommand(new PlaceOrderCommand(orderService, order));
        invoker.executeCommand();
        return order;
    }

    @DeleteMapping("/{orderId}")
    public Order cancelOrder(@PathVariable int orderId) {
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order not found!");
        }
        invoker.setCommand(new PlaceOrderCommand(orderService, orderId));
        invoker.undoCommand();
        return order;
    }

}
