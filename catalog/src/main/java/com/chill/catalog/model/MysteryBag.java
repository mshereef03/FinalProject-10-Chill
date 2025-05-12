package com.chill.catalog.model;

// import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.RequiredArgsConstructor;
// import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "mystery_bags")
//@NoArgsConstructor
//@RequiredArgsConstructor
//@Getter
//@Setter
public class MysteryBag {
    @Id
    private String id;
    private List<String> itemIds;
    private double basePrice;
    private Instant releaseAt;
    private Status status = Status.PENDING;
    private Size size;

    public enum Status   { PENDING, ACTIVE, SOLD_OUT }
    public enum Size { SMALL, MEDIUM, BIG }

    // Empty constructor
    public MysteryBag() {
    }

    // Constructor with all fields
    public MysteryBag(String id, List<String> itemIds, double basePrice, Instant releaseAt, Status status, Size size) {
        this.id = id;
        this.itemIds = itemIds;
        this.basePrice = basePrice;
        this.releaseAt = releaseAt;
        this.status = status;
        this.size = size;
    }

    // Constructor with all fields except id
    public MysteryBag(List<String> itemIds, double basePrice, Instant releaseAt, Status status, Size size) {
        this.itemIds = itemIds;
        this.basePrice = basePrice;
        this.releaseAt = releaseAt;
        this.status = status;
        this.size = size;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getItemIds() {
        return itemIds;
    }

    public void setItemIds(List<String> itemIds) {
        this.itemIds = itemIds;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public Instant getReleaseAt() {
        return releaseAt;
    }

    public void setReleaseAt(Instant releaseAt) {
        this.releaseAt = releaseAt;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }


}

