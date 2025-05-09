package com.chill.feedback.models;

import com.chill.feedback.dtos.FeedbackDTO;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "review")
public class Review extends Feedback {
    @Id
    private UUID id;

    @Column(name="userId")
    private UUID userId;

    @Column(name="vendorId")
    private UUID vendorId;

    @Column(name="orderId")
    private UUID orderId;

    @Column(name="comment")
    private String comment;

    @Column(name = "rating")
    private int rating;

    // Constructors
    public Review() { }
    public Review(UUID userId, UUID vendorId, UUID orderId, String comment, int rating) {
        this.userId = userId;
        this.vendorId = vendorId;
        this.orderId = orderId;
        this.comment = comment;
        this.rating = rating;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getVendorId() {
        return vendorId;
    }

    public void setVendorId(UUID vendorId) {
        this.vendorId = vendorId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public Feedback update(FeedbackDTO feedbackDTO) {
        this.userId = feedbackDTO.getUserId();
        this.vendorId = feedbackDTO.getVendorId();
        this.orderId = feedbackDTO.getOrderId();
        this.comment = feedbackDTO.getComment();
        this.rating = feedbackDTO.getRating();

        return this;
    }
}