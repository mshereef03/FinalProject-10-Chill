package com.chill.feedback.models;

import jakarta.persistence.Column;
import org.springframework.data.annotation.Id;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;
import java.util.Date;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Review.class, name = "review"),
        @JsonSubTypes.Type(value = Thread.class, name = "thread"),
        @JsonSubTypes.Type(value = Complaint.class, name = "complaint")
})
@Document(collection = "feedbacks")
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
    private Date createdAt;

    // Constructors
    public Feedback() {
        this.id = UUID.randomUUID();
        this.createdAt = new Date();
    }

    public Feedback(UUID userId, UUID vendorId, UUID orderId, String comment) {
        super();
        this.userId = userId;
        this.vendorId = vendorId;
        this.orderId = orderId;
        this.comment = comment;
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