package com.chill.order.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToMany(mappedBy = "cart")
    @JsonIgnore
    private List<Product> products = new ArrayList<>();
    @OneToOne(mappedBy = "cart")
    private Order order;

    public Cart() {

    }
    public Cart(List<Product> products) {
      this.products = products;
    }

    public Cart(List<Product> products, Order order) {
        this.products = products;
        this.order = order;
    }


    public int getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }


}
