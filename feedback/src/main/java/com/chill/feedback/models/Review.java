package com.chill.feedback.models;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Document("feedbacks")
public class Review extends Feedback implements Votable {
    private int rating;

    // record of who voted
    private Set<UUID> upvotedBy   = new HashSet<>();
    private Set<UUID> downvotedBy = new HashSet<>();

    public Review() { }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public void upvote(UUID userId) {
        if (upvotedBy.add(userId)) {
            // remove any prior downvote
            downvotedBy.remove(userId);
        }
    }

    @Override
    public void downvote(UUID userId) {
        if (downvotedBy.add(userId)) {
            upvotedBy.remove(userId);
        }
    }

    public int getUpvoteCount() {
        return upvotedBy.size();
    }

    public int getDownvoteCount() {
        return downvotedBy.size();
    }
}
