package com.chill.feedback.models;

import com.chill.feedback.dtos.FeedbackDTO;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;
import java.util.Date;

@Document(collection = "feedback")
public abstract class Feedback {
    @Id
    private UUID id;

    @Column(name = "userId")
    private UUID userId;

    @Column(name = "vendorId")
    private UUID vendorId;

    @Column(name = "orderId")
    private UUID orderId;

    @Column(name = "comment")
    private String comment;

    @Column(name = "createdAt")
    private Date createdAt = new Date();

    public abstract Feedback update(FeedbackDTO feedbackDTO);

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}