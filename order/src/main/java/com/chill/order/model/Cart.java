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

    private List<MysteryBagDTO> products = new ArrayList<>();
    @OneToOne(mappedBy = "cart")
    private Order order;

    public Cart() {

    }
    public Cart(List<MysteryBagDTO> products) {
      this.products = products;
    }

    public Cart(List<MysteryBagDTO> products, Order order) {
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

    public List<MysteryBagDTO> getProducts() {
        return products;
    }

    public void setProducts(List<MysteryBagDTO> products) {
        this.products = products;
    }


}
