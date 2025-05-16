package com.chill.feedback.models;

import jakarta.persistence.Column;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document("feedbacks")
public class Complaint extends Feedback {
    @Column(name = "tag")
    private Tag tag;

    // Constructors
    public Complaint() {}
    public Complaint(UUID userId, UUID vendorId, UUID orderId, String comment, Tag tag) {
        super(userId, vendorId, orderId, comment);
        this.tag = tag;
    }

    // Setters & Getters
    public Tag getTag() { return tag; }
    public void setTag(Tag tag) { this.tag = tag; }
}
