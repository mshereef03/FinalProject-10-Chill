package com.chill.order.controller;

import com.chill.order.client.MysteryBagClient;
import com.chill.order.model.Cart;
import com.chill.order.model.MysteryBagDTO;
import com.chill.order.model.Order;
import com.chill.order.repository.CartRepository;
import com.chill.order.service.CartService;
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
@RequestMapping("/orders")

public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartRepository cartRepository;

    private DiscountContext discountContext;

    @Autowired
    PromoCodeService promoCodeService;

    @Autowired
    private CartService cartService;

    @Autowired
    private MysteryBagClient mysteryBagClient;


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
//    @PostMapping
//    public Order placeOrder(@RequestBody Order order) {
//        System.out.println(order);
//        return orderService.placeOrder(order);
//    }

    @DeleteMapping("/{orderId}")
    public Order cancelOrder(@PathVariable int orderId) {
        try
        {
            Order order = orderService.cancelOrder(orderId);
            Cart cart = order.getCart();
            int cartId = cart.getId();
            List<MysteryBagDTO> products = cart.getProducts();
            for (MysteryBagDTO product : products) {
                mysteryBagClient.getMysteryBag(product.getId(),(-1));
            }
            cartService.deleteCart(cartId);
            return order;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // discount is percentage
    @PutMapping("/applyPromo/{orderId}")
    public Order applyPromo(@PathVariable("orderId") int orderId, @RequestBody String code) {
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order not found!");
        }

        int discount;
        try{
            System.out.println("Promocode Code: " + code);
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
        if (order.getCart() != null) {
            Cart freshCart = cartRepository.findById(order.getCart().getId())
                    .orElseThrow(() -> new RuntimeException("Cart not found"));
            order.setCart(freshCart);
        }
            discountContext = new DiscountContext(new BulkDiscountStrategy());
            double discountedPrice = discountContext.applyDiscount(order, 25);
            order.setPrice(discountedPrice);
            return orderService.updateOrder(order);

    }



}
