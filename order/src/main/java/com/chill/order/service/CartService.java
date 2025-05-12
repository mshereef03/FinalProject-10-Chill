package com.chill.order.service;

import com.chill.order.model.Cart;
import com.chill.order.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public List<Cart> findAllCarts() {
        return cartRepository.findAll();
    }
    @CachePut(value = "cart_cache", key = "#cart.id")
    public Cart addCart(Cart cart) {
        return cartRepository.save(cart);
    }
    @CacheEvict(value = "cart_cache", key = "#cartId")
    public void deleteCart(int cartId) {
        cartRepository.deleteById(cartId);
    }

    @Cacheable(value = "cart_cache", key = "#cartId")
    public Optional<Cart> findCartById(int cartId) {
        return cartRepository.findById(cartId);
    }

    @CachePut(value = "cart_cache", key = "#cart.id")
    public Cart updateCart(Cart cart) {
        return cartRepository.save(cart);
    }
}