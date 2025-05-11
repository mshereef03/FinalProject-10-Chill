package com.chill.feedback.models;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Document("feedbacks")
public class Thread extends Feedback implements Votable {
    private UUID parentId;

    // record of who voted
    private Set<UUID> upvotedBy   = new HashSet<>();
    private Set<UUID> downvotedBy = new HashSet<>();

    public Thread() { }

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

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }
}
