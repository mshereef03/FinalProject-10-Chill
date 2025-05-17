package com.chill.order.controller;

import com.chill.order.model.Order;
import com.chill.order.service.CommandPattern.CancelOrderCommand;
import com.chill.order.service.CommandPattern.Invoker;
import com.chill.order.service.CommandPattern.PlaceOrderCommand;
import com.chill.order.service.OrderService;
import com.chill.order.service.PromoCodeService;
import com.chill.order.service.StrategyPattern.DiscountContext;
import com.chill.order.service.StrategyPattern.BulkDiscountStrategy;
import com.chill.order.service.StrategyPattern.PromoCodeStrategy;
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
    private DiscountContext discountContext;


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
        return orderService.placeOrder(order);
    }

    @DeleteMapping("/{orderId}")
    public Order cancelOrder(@PathVariable int orderId) {
        return orderService.cancelOrder(orderId);
    }

    // discount is percentage
    @PutMapping("/applyPromo/{orderId}")
    public Order applyPromo(@PathVariable int orderId, @RequestBody String code) {
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order not found!");
        }
        PromoCodeService promoCodeService = new PromoCodeService();
        int discount;
        try{
            discount= promoCodeService.getDiscountByPromoCode(code);
            discountContext = new DiscountContext(new PromoCodeStrategy());
            double discountedPrice = discountContext.applyDiscount(order, discount);
            order.setPrice(discountedPrice);
            return orderService.updateOrder(order);
        }
        catch(Exception e){
             System.out.println(e.getMessage());
             return order;
        }


    }
    // discount is fixed number (money)
    @PutMapping("/applyBulk/{orderId}")
    public Order applyPromo(@PathVariable int orderId) {
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order not found!");
        }
            discountContext = new DiscountContext(new BulkDiscountStrategy());
            double discountedPrice = discountContext.applyDiscount(order, 25);
            order.setPrice(discountedPrice);
            return orderService.updateOrder(order);

    }



}
