package com.chill.order.model;


import jakarta.persistence.*;

@Entity
@Table(name = "promocode")
public class PromoCode {
    @Id
    private String code;
    int discount;
    public PromoCode() {

    }
    public PromoCode(String code, int discount) {
        this.code=code;
        this.discount = discount;
    }
    public PromoCode(int discount) {
        this.discount = discount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
