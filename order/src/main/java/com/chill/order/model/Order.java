package com.chill.order.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;
    private double price;

    @OneToOne (cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    @JsonIgnore
//    @JsonManagedReference
    private Cart cart;

    public Order() {

    }

    public Order(int id, Cart cart) {
        this.id = id;
        this.calculateTotal();
        this.cart = cart;
    }

    public Order(Cart cart) {
        this.calculateTotal();
        this.cart = cart;
    }

    public void calculateTotal(){
        String items= this.cart.getProductsJson();
        List<MysteryBagDTO> mysteryBags = cart.convertJsonToList();
        for(MysteryBagDTO mysteryBag: mysteryBags){
            this.price+=mysteryBag.getPrice();
        }

    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public Cart getCart() {
        return cart;
    }
    public void setCart(Cart cart) {
        this.cart = cart;
    }

}
