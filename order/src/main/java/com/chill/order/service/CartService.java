package com.chill.order.service;

import com.chill.order.client.MysteryBagClient;
import com.chill.order.model.Cart;
import com.chill.order.model.MysteryBagDTO;
import com.chill.order.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private MysteryBagClient mysteryBagClient;

    public List<Cart> findAllCarts() {
        return cartRepository.findAll();
    }
    @CachePut(value = "cart_cache", key = "#cart.id")
    public Cart addCart(Cart cart) {
        if (cart.getId() == 0) {
            cart = new Cart();
        }
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

    @CachePut(value = "cart_cache", key = "#cartId")
    public Cart addMysteryBagToCart(int cartId, String mysteryBagId) {

        try {

            double price = mysteryBagClient.getMysteryBag(mysteryBagId,1);

            MysteryBagDTO mysteryBag = new MysteryBagDTO(mysteryBagId, price);

            Cart cart = cartRepository.findById(cartId)
                    .orElseThrow(() -> new RuntimeException("Cart not found"));

            List<MysteryBagDTO> products = cart.getProducts();

            products.add(mysteryBag);
            cart.setProductsJson(cart.convertListToJson(products));

            return cartRepository.save(cart);


        } catch (Exception e) {
            throw new RuntimeException("Error fetching mystery bag: " + e.getMessage());
        }
    }
    @CachePut(value = "cart_cache", key = "#cartId")
    public Cart removeMysteryBagFromCart(int cartId, String mysteryBagId) {

        try {

            double price = mysteryBagClient.getMysteryBag(mysteryBagId,(-1));

            Cart cart = cartRepository.findById(cartId)
                    .orElseThrow(() -> new RuntimeException("Cart not found"));


            List<MysteryBagDTO> products = cart.getProducts();
            System.out.println("Before: " +products.size());
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getId().equals(mysteryBagId)) {
                    products.remove(i);
                    break;
                }
            }
            System.out.println("After: " +products.size());
            String productsJson = cart.convertListToJson(products);


            cart.setProductsJson(productsJson);

            return cartRepository.save(cart);


        } catch (Exception e) {
            throw new RuntimeException("Error fetching mystery bag: " + e.getMessage());
        }
    }







}