package com.chill.order.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "mystery_bags", columnDefinition = "jsonb")  // Use jsonb to store the list as JSON
    @JdbcTypeCode(SqlTypes.JSON)
    private String productsJson;
    @OneToOne(mappedBy = "cart")
    @JsonIgnore
    private Order order;

    public Cart() {
        this.productsJson = "[]";

    }

    public Cart(int id) {
        this.id = id;
        this.productsJson = "[]";
    }
    public Cart(int id,Order order){
        this.id = id;
        this.order = order;
        this.productsJson = "[]";
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
    public String getProductsJson() {
        return productsJson;
    }
    public void setProductsJson(String productsJson) {
        this.productsJson = productsJson;
    }


    public String convertListToJson(List<MysteryBagDTO> products) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(products);  // Convert List<MysteryBagDTO> to JSON string
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<MysteryBagDTO> convertJsonToList() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(this.productsJson, new TypeReference<List<MysteryBagDTO>>() {
            });  // Convert JSON string back to List<MysteryBagDTO>
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

}
