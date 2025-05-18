//package com.chill.order.model;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.persistence.*;
//import org.hibernate.annotations.JdbcTypeCode;
//import org.hibernate.type.SqlTypes;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//@Table(name = "cart")
//public class Cart {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//    @Column(name = "mystery_bags", columnDefinition = "jsonb")  // Use jsonb to store the list as JSON
//    @JdbcTypeCode(SqlTypes.JSON)
//    private String productsJson;
//    @OneToOne(mappedBy = "cart")
//    @JsonIgnore
//    private Order order;
//
//    public Cart() {
//        this.productsJson = "[]";
//
//    }
//
//    public Cart(int id) {
//        this.id = id;
//        this.productsJson = "[]";
//    }
//    public Cart(int id,Order order){
//        this.id = id;
//        this.order = order;
//        this.productsJson = "[]";
//    }
//
//    public Cart(String productJson) {
//        this.productsJson = productJson;
//    }
//
//    public Cart(String productJson, Order order) {
//        this.productsJson = productJson;
//        this.order = order;
//    }
//
//
//    public int getId() {
//        return id;
//    }
//
//    public Order getOrder() {
//        return order;
//    }
//
//    public void setOrder(Order order) {
//        this.order = order;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//    public String getProductsJson() {
//        return productsJson;
//    }
//    public void setProductsJson(String productsJson) {
//        this.productsJson = productsJson;
//    }
//
//
//    public String convertListToJson(List<MysteryBagDTO> products) {
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            return mapper.writeValueAsString(products);  // Convert List<MysteryBagDTO> to JSON string
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public List<MysteryBagDTO> convertJsonToList() {
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            return mapper.readValue(this.productsJson, new TypeReference<List<MysteryBagDTO>>() {
//            });  // Convert JSON string back to List<MysteryBagDTO>
//        } catch (IOException e) {
//            e.printStackTrace();
//            return new ArrayList<>();
//        }
//
//    }
//
//}
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

    @Column(name = "mystery_bags", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String productsJson = "[]";

    @Transient
    @JsonIgnore
    private List<MysteryBagDTO> products = new ArrayList<>();

    @OneToOne(mappedBy = "cart")
    @JsonBackReference
    private Order order;

    public Cart() {
    }

    public Cart(int id) {
        this.id = id;
    }

    public Cart(int id, Order order) {
        this.id = id;
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    /**
     * Expose the deserialized list of MysteryBagDTOs.
     */
    public List<MysteryBagDTO> getProducts() {
        if (products == null || products.isEmpty()) {
            products = convertJsonToList();
        }
        return products;
    }

    /**
     * Set the list and update the JSON column accordingly.
     */
    public void setProducts(List<MysteryBagDTO> products) {
        this.products = products;
        this.productsJson = convertListToJson(products);
    }

    public String getProductsJson() {
        return productsJson;
    }

    public void setProductsJson(String productsJson) {
        this.productsJson = productsJson;
    }

    /**
     * Before saving, ensure JSON matches current products list.
     */
    @PrePersist
    @PreUpdate
    public void prePersist() {
        this.productsJson = convertListToJson(this.products);
    }

    /**
     * After loading, initialize the products list from JSON.
     */
    @PostLoad
    public void postLoad() {
        this.products = convertJsonToList();
    }

    // Utility methods for conversion
    public String convertListToJson(List<MysteryBagDTO> list) {
        try {
            return new ObjectMapper().writeValueAsString(list);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "[]";
        }
    }

    public List<MysteryBagDTO> convertJsonToList() {
        try {
            return new ObjectMapper().readValue(this.productsJson, new TypeReference<List<MysteryBagDTO>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}