package service;
import model.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.CartRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public List<Cart> findAllCarts() {
        return cartRepository.findAll();
    }
    public Cart addCart(Cart cart) {
        return cartRepository.save(cart);
    }
    public void deleteCart(int cartId) {
        cartRepository.deleteById(cartId);
    }
    public Optional<Cart> findCartById(int cartId) {
        return cartRepository.findById(cartId);
    }

    public Cart updateCart(Cart cart) {
        return cartRepository.save(cart);
    }

}
