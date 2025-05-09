package com.chill.feedback.models;

import com.chill.feedback.dtos.FeedbackDTO;
import jakarta.persistence.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

@Document(collection = "complaint")
public class Complaint extends Feedback {
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

    @Column(name="tag")
    private Tag tag;

    // Constructors
    public Complaint() {}
    public Complaint(UUID userId, UUID vendorId, UUID orderId, String comment, Tag tag) {
        this.userId = userId;
        this.vendorId = vendorId;
        this.orderId = orderId;
        this.comment = comment;
        this.tag = tag;
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

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    @Override
    public Feedback update(FeedbackDTO feedbackDTO) {
        this.userId = feedbackDTO.getUserId();
        this.vendorId = feedbackDTO.getVendorId();
        this.orderId = feedbackDTO.getOrderId();
        this.comment = feedbackDTO.getComment();
        this.tag = feedbackDTO.getTag();

        return this;
    }
}