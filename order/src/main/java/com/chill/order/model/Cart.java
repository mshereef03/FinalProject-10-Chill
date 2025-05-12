package com.chill.order.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "mysteryBags", columnDefinition = "jsonb")  // Use jsonb to store the list as JSON
    private String productsJson;
    @OneToOne(mappedBy = "cart")
    private Order order;

    public Cart() {

    }

    public Cart(String productJson) {
        this.productsJson = productJson;
    }

    public Cart(String productJson, Order order) {
        this.productsJson = productJson;
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


    private String convertListToJson(List<MysteryBagDTO> products) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(products);  // Convert List<MysteryBagDTO> to JSON string
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<MysteryBagDTO> convertJsonToList(String productsJson) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(productsJson, new TypeReference<List<MysteryBagDTO>>() {
            });  // Convert JSON string back to List<MysteryBagDTO>
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
