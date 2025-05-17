package com.chill.user.dto;

import com.chill.user.model.Tag;

import java.util.UUID;

public class FeedbackDTO {
    // Feedback
    private UUID id;
    private UUID userId;
    private UUID vendorId;
    private UUID orderId;
    String comment;
    Tag tag;

    int rating;

    UUID parentId;

    // Constructors
    public FeedbackDTO() {}
    public FeedbackDTO(UUID id, UUID userId, UUID vendorId, UUID orderId, String comment, Tag tag, int rating, UUID parentId) {
        this.id = id;
        this.userId = userId;
        this.vendorId = vendorId;
        this.orderId = orderId;
        this.comment = comment;
        this.tag = tag;
        this.rating = rating;
        this.parentId = parentId;
    }
    public FeedbackDTO(UUID userId, UUID vendorId, UUID orderId, String comment, Tag tag, int rating, UUID parentId) {
        this.userId = userId;
        this.vendorId = vendorId;
        this.orderId = orderId;
        this.comment = comment;
        this.tag = tag;
        this.rating = rating;
        this.parentId = parentId;
    }

    // Getters and Setters
    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getVendorId() {
        return vendorId;
    }

    public void setVendorId(UUID vendorId) {
        this.vendorId = vendorId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
