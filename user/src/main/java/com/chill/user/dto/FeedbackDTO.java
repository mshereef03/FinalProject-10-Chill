package com.chill.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;


import java.util.Date;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeedbackDTO {
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    private String type;
    private UUID id;
    private UUID userId;
    private UUID vendorId;
    private UUID orderId;
    private String comment;
    private Date createdAt;

    // only present when type == REVIEW
    private Integer rating;

    // only present when type == THREAD
    private UUID parentId;

    // Constructors
    public FeedbackDTO() {
        // default
    }

    public FeedbackDTO(String type, UUID id, UUID userId, UUID vendorId, UUID orderId, String comment, Date createdAt) {
        this.type = type;
        this.id = id;
        this.userId = userId;
        this.vendorId = vendorId;
        this.orderId = orderId;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    // Getters & Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
