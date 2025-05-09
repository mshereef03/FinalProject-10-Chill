package com.chill.feedback.models;

import com.chill.feedback.dtos.FeedbackDTO;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Document(collection = "thread")
public class Thread extends Feedback {
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

    @Column(name = "parentId")
    private UUID parentId;

    // private boolean answered; // not needed

    // Constructors
    public Thread() { }
    public Thread(UUID userId, UUID vendorId, UUID orderId, String comment, UUID parentId) {
        this.userId = userId;
        this.vendorId = vendorId;
        this.orderId = orderId;
        this.comment = comment;
        this.parentId = parentId;
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

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    @Override
    public Feedback update(FeedbackDTO feedbackDTO) {
        this.userId = feedbackDTO.getUserId();
        this.vendorId = feedbackDTO.getVendorId();
        this.orderId = feedbackDTO.getOrderId();
        this.comment = feedbackDTO.getComment();
        this.parentId = feedbackDTO.getParentId();

        return this;
    }
}