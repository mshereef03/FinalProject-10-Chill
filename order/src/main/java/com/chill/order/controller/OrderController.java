package com.chill.order.controller;

import com.chill.order.model.Cart;
import com.chill.order.model.Order;
import com.chill.order.service.CommandPattern.Invoker;
import com.chill.order.service.CommandPattern.PlaceOrderCommand;
import com.chill.order.service.OrderService;
import com.chill.order.service.StrategyPattern.DiscountContext;
import com.chill.order.service.StrategyPattern.FlatDiscountStrategy;
import com.chill.order.service.StrategyPattern.NoDiscountStrategy;
import com.chill.order.service.StrategyPattern.PercentageDiscountStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/order")

public class OrderController {

    @Autowired
    private OrderService orderService;

    private Invoker invoker;

    private DiscountContext discountContext;

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

    @PutMapping
    public Order updateOrder(@RequestBody Order order) {
        int id=order.getId();
        Order oldOrder = orderService.getOrderById(id);
        if(oldOrder != null) {
           return orderService.updateOrder(order);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Order to update not found!");
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

    // very very temp need to decide how to do it
    @PutMapping("/{orderId}/apply-discount")
    public Order applyDiscount(@PathVariable int orderId, @RequestParam String strategy, @RequestParam double discount) {
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order not found!");
        }


        switch (strategy.toLowerCase()) {
            case "percentage":
                discountContext = new DiscountContext(new PercentageDiscountStrategy());
                break;
            case "flat":
                discountContext = new DiscountContext(new FlatDiscountStrategy());
                break;
            case "none":
            default:
                discountContext = new DiscountContext(new NoDiscountStrategy());
                break;
        }
        double discountedPrice = discountContext.applyDiscount(order.getPrice(), discount);
        order.setPrice(discountedPrice);
        return orderService.updateOrder(order);
    }



}
