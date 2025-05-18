package com.chill.catalog.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "mystery_bags")
public class MysteryBag {
    @Id
    private String id;
    private List<String> itemIds;
    private double basePrice;
    private Status status;
    private Size size;
    private int quantity;
    private String strategyCode;

    public enum Status   { PENDING, ACTIVE, SOLD_OUT }
    public enum Size { SMALL, MEDIUM, BIG }

    public MysteryBag() {
    }

    public MysteryBag(String id, List<String> itemIds, Size size, int quantity, String strategyCode) {
        this.id = id;
        this.itemIds = itemIds;
        this.basePrice = 0;
        this.status = Status.PENDING;
        this.size = size;
        this.quantity = quantity;
        this.strategyCode = strategyCode;
    }

    public MysteryBag(List<String> itemIds, Size size, int quantity, String strategyCode) {
        this.itemIds = itemIds;
        this.basePrice = 0;
        this.status = Status.PENDING;
        this.size = size;
        this.quantity = quantity;
        this.strategyCode = strategyCode;
    }

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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public String getStrategyCode() {
        return strategyCode;
    }

    public void setStrategyCode(String strategyCode) {
        this.strategyCode = strategyCode;
    }
}

