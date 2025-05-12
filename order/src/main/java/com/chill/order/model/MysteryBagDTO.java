package com.chill.order.model;

public class MysteryBagDTO {
    private String id;
    private double basePrice;

    public MysteryBagDTO() {
    }

    public MysteryBagDTO( int price) {
        this.basePrice = price;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPrice() {
        return basePrice;
    }

    public void setPrice(double price) {
        this.basePrice = price;
    }
}
