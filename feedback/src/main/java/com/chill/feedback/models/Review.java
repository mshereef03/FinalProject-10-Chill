package com.chill.feedback.models;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Document("feedbacks")
public class Review extends Feedback implements Votable {
    private int rating = 1; // default rating
    private Set<UUID> upvotedSet   = new HashSet<>();
    private Set<UUID> downvotedSet = new HashSet<>();

    // Constructors
    public Review() { }
    public Review(UUID userId, UUID vendorId, UUID orderId, String comment, int rating) {
        super(userId, vendorId, orderId, comment);
        this.rating = rating;
    }
    public Review(UUID userId, UUID vendorId, UUID orderId, String comment) {
        super(userId, vendorId, orderId, comment);
        this.rating = 1;
    }

    // Getters & Setters
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public void upvote(UUID userId) {
        if (upvotedSet.add(userId)) {
            downvotedSet.remove(userId);
        }
    }

    @Override
    public void downvote(UUID userId) {
        if (downvotedSet.add(userId)) {
            upvotedSet.remove(userId);
        }
    }

    public int getUpvoteCount() {
        return upvotedSet.size();
    }

    public int getDownvoteCount() {
        return downvotedSet.size();
    }
}
