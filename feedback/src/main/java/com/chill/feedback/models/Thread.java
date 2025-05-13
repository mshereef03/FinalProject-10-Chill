package com.chill.feedback.models;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Document("feedbacks")
public class Thread extends Feedback implements Votable, Replyable {
    private UUID parentId;

    private Set<UUID> upvotedSet   = new HashSet<>();
    private Set<UUID> downvotedSet = new HashSet<>();

    public Thread() { }

    @Override
    public void upvote(UUID userId)
    {
        if (upvotedSet.add(userId)) {
            downvotedSet.remove(userId);
        }
    }

    @Override
    public void downvote(UUID userId)
    {
        if (downvotedSet.add(userId)) {
            upvotedSet.remove(userId);
        }
    }

    public int getUpvoteCount()
    {
        return upvotedSet.size();
    }

    public int getDownvoteCount()
    {
        return downvotedSet.size();
    }

    public UUID getParentId()
    {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

}
