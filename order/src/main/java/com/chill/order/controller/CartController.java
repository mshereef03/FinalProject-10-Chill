package com.chill.order.controller;
import com.chill.order.model.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.chill.order.service.CartService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping
    public List<Cart> getAllCarts(){
        return cartService.findAllCarts();
    }
    @GetMapping("/{cartId}")
    public Cart getCartById(@PathVariable int cartId) {
        Optional<Cart> cart= cartService.findCartById(cartId);
        if(cart.isPresent()) {
            return cart.get();
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Cart Id not found!");

    }
    @PostMapping
    public Cart addCart(Cart cart) {
        return cartService.addCart(cart);
    }
    @PutMapping
    public Cart updateCart(@RequestBody Cart cart) {
         int id=cart.getId();
         Optional<Cart> cart1= cartService.findCartById(id);
         if(cart1.isPresent()) {
             return cartService.updateCart(cart);
         }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Cart to update not found!");
    }

    @DeleteMapping("/{cartId}")
    public void deleteCartByID(@PathVariable int cartId) {
        cartService.deleteCart(cartId);
    }


}
